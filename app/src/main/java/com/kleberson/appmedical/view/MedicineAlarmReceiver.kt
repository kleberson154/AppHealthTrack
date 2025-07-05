package com.kleberson.appmedical.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.kleberson.appmedical.R

class MedicineAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val medicineName = intent.getStringExtra("medicine_name") ?: "Medicamento"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("medicine_channel", "Lembrete de Medicamento", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, "medicine_channel")
            .setContentTitle("Hora do medicamento")
            .setContentText("Está na hora de tomar: $medicineName")
            .setSmallIcon(R.drawable.ic_medicine)
            .build()

        notificationManager.notify(1, notification)
    }
}