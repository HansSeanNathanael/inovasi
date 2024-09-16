package com.example.inovasi.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.inovasi.model.TaskModel
import java.util.concurrent.TimeUnit


class SendFirebaseMessage {

    fun send(context : Context, taskModel : TaskModel) {

        val diffInMillies = taskModel.date.time - taskModel.reminder.time
        val diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillies)

        if (diffInMinutes > 0) {
            val intent = Intent(context, NotificationReceiver::class.java).apply {
                putExtra("notification_id", taskModel.id)
                putExtra("title", taskModel.subject)
                putExtra("message", taskModel.date.toString())
            }

            val pendingIntent = PendingIntent.getBroadcast(context, 145, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val triggerTime = taskModel.reminder.time

            if (android.os.Build.VERSION.SDK_INT >= 31) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                }
            }
        }
    }
}