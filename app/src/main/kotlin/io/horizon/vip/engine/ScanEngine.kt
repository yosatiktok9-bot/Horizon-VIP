package io.horizon.vip.engine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocket
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Serializable
data class Finding(
    val type: String,
    val target: String,
    val detail: String,
    val severity: String = "info"
)

@Serializable
data class ScanResult(
    val mode: String,
    val target: String,
    val findings: List<Finding>,
    val startedAt: Long,
    val finishedAt: Long
)

@Singleton
class ScanEngine @Inject constructor() {

    private val _log = MutableSharedFlow<String>(extraBufferCapacity = 256)
    val log: SharedFlow<String> = _log

    private val client = OkHttpClient.Builder()
        .connectTimeout(4, TimeUnit.SECONDS)
        .readTimeout(6, TimeUnit.SECONDS)
        .followRedirects(true)
        .build()

    private val json = Json { ignoreUnknownKeys = true; prettyPrint = true }

    private suspend fun emit(msg: String) {
        _log.emit(msg)
    }

    suspend fun runScan(
        mode: String,
        target: String,
        concurrency: Int = 64,
        timeoutSec: Float = 2f
    ): ScanResult = withContext(Dispatchers.IO) {
        val started = System.currentTimeMillis()
        emit("scan start mode=$mode target=$target")
        val findings = when (mode.lowercase()) {
            "dns" -> scanDns(target)
            "ports" -> scanPorts(target, concurrency, timeoutSec)
            "http" -> scanHttp(target)
            "tls" -> scanTls(target)
            "full" -> {
                scanDns(target) + scanPorts(target, concurrency, timeoutSec) +
                    scanHttp(target) + scanTls(target)
            }
            else -> scanDns(target) + scanHttp(target)
        }
        val finished = System.currentTimeMillis()
        emit("scan done findings=${findings.size} ms=${finished - started}")
        ScanResult(mode, target, findings, started, finished)
    }

    fun toJson(result: ScanResult): String = json.encodeToString(result)

    private suspend fun scanDns(target: String): List<Finding> {
        val out = mutableListOf<Finding>()
        try {
            emit("dns resolve $target")
            val addrs = InetAddress.getAllByName(target.trim())
            addrs.forEach { a ->
                out += Finding("dns", target, a.hostAddress ?: a.toString(), "info")
                emit("  A/AAAA ${a.hostAddress}")
            }
            if (addrs.isEmpty()) {
                out += Finding("dns", target, "no records", "warn")
            }
        } catch (e: Exception) {
            emit("dns fail ${e.message}")
            out += Finding("dns", target, e.message ?: "resolve failed", "error")
        }
        return out
    }

    private suspend fun scanPorts(
        target: String,
        concurrency: Int,
        timeoutSec: Float
    ): List<Finding> = coroutineScope {
        val host = target.trim().removePrefix("http://").removePrefix("https://").substringBefore("/")
        val ports = listOf(
            21, 22, 23, 25, 53, 80, 110, 111, 135, 139, 143, 443, 445, 993, 995,
            1433, 1521, 3306, 3389, 5432, 5900, 6379, 8080, 8443, 27017
        )
        emit("ports scan $host ports=${ports.size}")
        val timeoutMs = (timeoutSec * 1000).toInt().coerceIn(500, 8000)
        ports.map { port ->
            async(Dispatchers.IO) {
                try {
                    Socket().use { s ->
                        s.connect(InetSocketAddress(host, port), timeoutMs)
                        emit("  open $host:$port")
                        Finding("port", "$host:$port", "open", "info")
                    }
                } catch (_: Exception) {
                    null
                }
            }
        }.awaitAll().filterNotNull()
    }

    private suspend fun scanHttp(target: String): List<Finding> {
        val out = mutableListOf<Finding>()
        val base = if (target.startsWith("http")) target else "http://$target"
        val https = if (target.startsWith("http")) target.replace("http://", "https://")
        else "https://$target"
        listOf(base, https).forEach { url ->
            try {
                emit("http GET $url")
                val req = Request.Builder().url(url).header("User-Agent", "HorizonVIP/2.0").build()
                client.newCall(req).execute().use { resp ->
                    val server = resp.header("Server") ?: "-"
                    val title = resp.body?.string()?.let { body ->
                        Regex("<title>(.*?)</title>", RegexOption.IGNORE_CASE)
                            .find(body)?.groupValues?.getOrNull(1)?.take(80)
                    } ?: "-"
                    out += Finding(
                        "http",
                        url,
                        "status=${resp.code} server=$server title=$title",
                        if (resp.code >= 500) "warn" else "info"
                    )
                    emit("  ${resp.code} $server")
                }
            } catch (e: Exception) {
                emit("http fail $url ${e.message}")
                out += Finding("http", url, e.message ?: "fail", "error")
            }
        }
        return out
    }

    private suspend fun scanTls(target: String): List<Finding> {
        val out = mutableListOf<Finding>()
        val host = target.trim().removePrefix("http://").removePrefix("https://")
            .substringBefore("/").substringBefore(":")
        try {
            emit("tls probe $host:443")
            val trustAll = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(c: Array<X509Certificate>?, a: String?) {}
                override fun checkServerTrusted(c: Array<X509Certificate>?, a: String?) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })
            val ctx = SSLContext.getInstance("TLS")
            ctx.init(null, trustAll, java.security.SecureRandom())
            (ctx.socketFactory.createSocket(host, 443) as SSLSocket).use { sock ->
                sock.soTimeout = 5000
                sock.startHandshake()
                val session = sock.session
                val peer = session.peerCertificates.firstOrNull() as? X509Certificate
                val cn = peer?.subjectX500Principal?.name ?: "-"
                val proto = session.protocol
                val cipher = session.cipherSuite
                out += Finding("tls", host, "proto=$proto cipher=$cipher cn=$cn", "info")
                emit("  $proto $cipher")
                peer?.let {
                    out += Finding(
                        "tls_cert",
                        host,
                        "notBefore=${it.notBefore} notAfter=${it.notAfter}",
                        "info"
                    )
                }
            }
        } catch (e: Exception) {
            emit("tls fail ${e.message}")
            out += Finding("tls", host, e.message ?: "fail", "error")
        }
        return out
    }
}
