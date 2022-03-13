package com.example.tritimer

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

 fun MainActivity.cancelAlarm() {
    MainActivity.alarmManager = getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
    val intent = Intent(this, AlarmReceiver::class.java)

    MainActivity.pendingIntent = PendingIntent.getBroadcast(this, 0 ,intent, 0)

    MainActivity.alarmManager.cancel(MainActivity.pendingIntent)

    Toast.makeText(this, "alarm cancelled", Toast.LENGTH_SHORT).show()
}

 fun MainActivity.setAlarm() {
    MainActivity.alarmManager = getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager

    val intent = Intent(this, AlarmReceiver::class.java)
    MainActivity.pendingIntent = PendingIntent.getBroadcast(this, 0 ,intent, 0)
    MainActivity.alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP, MainActivity.calendar.timeInMillis, AlarmManager.INTERVAL_DAY, MainActivity.pendingIntent
    )
//        Toast.makeText(this, "alarm set success", Toast.LENGTH_SHORT).show()

}

 fun MainActivity.slideTimePicker(view: View) {
    var setHour = getHour(view.x).toInt()
    var setMinute = getMinute(view.x).toInt()
    if(!MainActivity.am){
        setHour += 12
    }
    MainActivity.calendar = Calendar.getInstance()
    MainActivity.calendar[Calendar.HOUR_OF_DAY] = setHour
    MainActivity.calendar[Calendar.MINUTE] = setMinute
    MainActivity.calendar[Calendar.SECOND] = 0
    MainActivity.calendar[Calendar.MILLISECOND] = 0
    Toast.makeText(this, "${setHour}:${setMinute
    }", Toast.LENGTH_SHORT).show()
}

// create channel for notification.
 fun MainActivity.createNotificationChannel() {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val name : CharSequence = "alarm"
        val desc = "channel for alarm"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("notficationChannel", name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}
