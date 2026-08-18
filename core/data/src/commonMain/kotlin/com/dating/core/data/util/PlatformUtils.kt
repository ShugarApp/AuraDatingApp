package com.dating.core.data.util

expect object PlatformUtils {
    fun getOSName(): String

    /** RN-2.5 — identificador estable por instalación/dispositivo para el chequeo de baneo. */
    fun getDeviceId(): String?
}
