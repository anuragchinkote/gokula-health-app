package com.gokulahealth

import android.app.Application
import com.gokulahealth.notification.NotificationHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GokulaHealthApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Create notification channel for vaccination alerts
        NotificationHelper(this).createNotificationChannel()
    }
}
