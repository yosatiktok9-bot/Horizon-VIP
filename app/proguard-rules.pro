# Horizon VIP ProGuard rules
-keep class com.chaquo.python.** { *; }
-keep class io.horizon.vip.nativebridge.NativePacket { *; }
-keepclassmembers class io.horizon.vip.nativebridge.NativePacket {
    native <methods>;
}
-keep class io.horizon.vip.bridge.** { *; }
-dontwarn com.chaquo.python.**
-keepattributes *Annotation*
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class ** {
    @kotlinx.serialization.Serializable <fields>;
}
