package com.example.notification

import android.app.*
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notification.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private var notificationManager : NotificationManager? = null
    private var CHANNEL_ID = "channel_id"
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Register Channel kedalam sistem
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(CHANNEL_ID,"Countdown","Ini merupakan deskripsi")

        binding.btnStart.setOnClickListener {
            countDownTimer.start()
        }

        countDownTimer = object : CountDownTimer(10000,1000){
            override fun onTick(p0: Long) {
                // masukkan text dari string
                // jika hitungan sudah nol maka masuk on finish
                binding.timer.text = getString(R.string.time_reamining,p0/1000)
            }

            override fun onFinish() {
                displayNotification()
            }

        }

    }

    private fun displayNotification(){
        // pertama buat val seperti dibawah
        // ini buat nampilkan notif

        val intent = Intent(this, Result_Activity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val notificationID = 45

        val snoozeIntent = Intent(this, Result_Activity::class.java).apply {
            putExtra(EXTRA_NOTIFICATION_ID, 0)
        }
        val snoozePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, snoozeIntent, 0)

        var notification = NotificationCompat.Builder(this,CHANNEL_ID)
                // ini isi notifnya
            .setContentTitle("coundont Timer")
            .setContentText("you timer end")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .addAction(android.R.drawable.ic_dialog_info, getString(R.string.snooze),
                snoozePendingIntent)
            .build()
        notificationManager?.notify(notificationID,notification)
    }

    private fun createNotificationChannel(id:String,name:String,channelDescription: String){
        // validasi notif akan dibuat jika version sdk 26+
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id, name, importance).apply {
                description = channelDescription
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }
}

