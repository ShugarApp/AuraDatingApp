package com.dating.core.data.util

import platform.UIKit.UIDevice

actual object PlatformUtils {
    actual fun getOSName() = "IOS"

    actual fun getDeviceId(): String? =
        UIDevice.currentDevice.identifierForVendor?.UUIDString
}
