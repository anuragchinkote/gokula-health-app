package com.gokulahealth.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Receiver to reschedule vaccination alerts when the device is rebooted.
 * In a production app, this would query the database for upcoming vaccinations
 * and re-register them with the AlarmManager.
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Logic to reschedule alarms would go here
            // This requires access to the database which usually involves
            // starting a Worker or using Hilt to inject a Repository.
        }
    }
}
