package com.example.whatsappnotificationreceiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.whatsappnotificationreceiver.NotificationListener.Companion.TAG


class MyService : Service() {
    var counter = 0
    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) startMyOwnForeground() else startForeground(
            1, Notification()
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val NOTIFICATION_CHANNEL_ID = "example.kNesar"
        val channelName = "Background Service"
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?)!!
        manager.createNotificationChannel(chan)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification: Notification =
            notificationBuilder.setOngoing(true).setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE).build()
        startForeground(2, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // val broadcastIntent = Intent()
        // broadcastIntent.action = "restartservice"
        // broadcastIntent.setClass(this, Restarter::class.java)
        // this.sendBroadcast(broadcastIntent)
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    class Restarter : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.i("Broadcast Listened", "Service tried to stop")
            val from = intent.getStringExtra("from")
            val message = intent.getStringExtra("message")
            val isImage = intent.getBooleanExtra("isImage", false)
            // val imageByteArray = intent.getByteArrayExtra("image")
//            val modelClass = if (isImage) {
//                val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray!!.size)
//                ModelClass(from.toString(), message.toString(), true, bitmap)
//            } else {
            val modelClass = ModelClass(from.toString(), message.toString(), false, null)
            //  }

            if (!MainActivity.myMsgList.contains(modelClass)) {
                MainActivity.myMsgList.add(modelClass)
            }
            MainActivity.rvMsg?.adapter =
                WhatsappNotificationAdapter(MainActivity.myMsgList, context)
            MainActivity.rvMsg?.adapter?.notifyDataSetChanged()
            Log.i(TAG, "From: $from")
            Log.i(TAG, "Message: $message")
        }
    }
}
