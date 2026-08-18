package com.dating.core.data.util

import android.provider.Settings
import org.koin.core.context.GlobalContext

actual object PlatformUtils {
    actual fun getOSName() = "ANDROID"

    actual fun getDeviceId(): String? = runCatching {
        val context = GlobalContext.get().get<android.app.Application>()
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }.getOrNull()
}
