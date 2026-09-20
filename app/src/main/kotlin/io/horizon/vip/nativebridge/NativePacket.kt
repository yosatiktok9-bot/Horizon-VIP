package io.horizon.vip.nativebridge

object NativePacket {
    init {
        System.loadLibrary("horizonvip")
    }

    external fun parseIpHeader(packet: ByteArray): String
    external fun fastPortScan(ip: String, ports: IntArray, timeoutMs: Int): IntArray
    external fun computeChecksum(data: ByteArray): Int
}
