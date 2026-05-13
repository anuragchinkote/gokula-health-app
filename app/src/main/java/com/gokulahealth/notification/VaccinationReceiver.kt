package com.gokulahealth.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class VaccinationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Vaccination Reminder"
        val message = intent.getStringExtra("message") ?: "Cattle vaccination is due today."
        
        val notificationHelper = NotificationHelper(context)
        notificationHelper.showNotification(title, message)
    }
}
