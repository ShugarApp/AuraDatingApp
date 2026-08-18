package com.dating.core.data.util

actual object PlatformUtils {
    actual fun getOSName(): String {
        return System.getProperty("os.name")
    }

    // Desktop no es el objetivo principal; no exponemos un identificador de dispositivo.
    actual fun getDeviceId(): String? = null
}
