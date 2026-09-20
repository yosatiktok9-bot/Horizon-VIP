#include <jni.h>
#include <stdint.h>
#include <arpa/inet.h>

extern "C" JNIEXPORT jint JNICALL
Java_io_horizon_vip_nativebridge_NativePacket_computeChecksum(
        JNIEnv* env, jclass, jbyteArray data) {
    jsize len = env->GetArrayLength(data);
    jbyte* bytes = env->GetByteArrayElements(data, nullptr);
    if (!bytes) return 0;

    uint32_t sum = 0;
    const uint16_t* words = reinterpret_cast<const uint16_t*>(bytes);
    int n = len / 2;
    for (int i = 0; i < n; ++i) {
        sum += ntohs(words[i]);
    }
    if (len & 1) {
        sum += static_cast<uint8_t>(bytes[len - 1]) << 8;
    }
    while (sum >> 16) {
        sum = (sum & 0xFFFF) + (sum >> 16);
    }
    env->ReleaseByteArrayElements(data, bytes, JNI_ABORT);
    return static_cast<jint>(~sum & 0xFFFF);
}
