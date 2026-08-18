package com.dating.home.presentation.chat.share

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

@Composable
actual fun rememberPlanShareLauncher(): (planText: String) -> Unit {
    return remember {
        { planText ->
            val controller = UIActivityViewController(
                activityItems = listOf(planText),
                applicationActivities = null
            )
            UIApplication.sharedApplication.keyWindow?.rootViewController
                ?.presentViewController(controller, animated = true, completion = null)
        }
    }
}
