package com.example.whatsappnotificationreceiver

import android.R.attr.bitmap
import android.app.Notification
import android.content.Intent
import android.graphics.Bitmap
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.whatsappnotificationreceiver.MyService.Restarter
import java.io.ByteArrayOutputStream


class NotificationListener : NotificationListenerService() {

    interface ListenMessages {
        fun onMessagesReceived()
    }

    override fun onListenerConnected() {
        Log.i(TAG, "Notification Listener connected")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.packageName != WA_PACKAGE) return
        notification = sbn.notification
        val bundle = notification?.extras
        val from = bundle?.getString(NotificationCompat.EXTRA_TITLE)
        val message = bundle?.getString(NotificationCompat.EXTRA_TEXT)
        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.putExtra("from", from)

        Log.d(
            TAG,
            "onNotificationPosted1:${bundle?.containsKey(NotificationCompat.EXTRA_BACKGROUND_IMAGE_URI)} "
        )
        Log.d(
            TAG,
            "onNotificationPosted2:${bundle?.containsKey(NotificationCompat.EXTRA_SHOW_BIG_PICTURE_WHEN_COLLAPSED)} "
        )
        Log.d(
            TAG,
            "onNotificationPosted3:${bundle?.containsKey(NotificationCompat.EXTRA_PICTURE)} "
        )
        Log.d(
            TAG,
            "onNotificationPosted_EXTRA_LARGE_ICON_BIG:${bundle?.containsKey(NotificationCompat.EXTRA_LARGE_ICON_BIG)} "
        )
        Log.d(
            TAG,
            "onNotificationPosted_EXTRA_LARGE_ICON:${bundle?.containsKey(NotificationCompat.EXTRA_LARGE_ICON)} "
        )
        // if (bundle?.containsKey(NotificationCompat.EXTRA_LARGE_ICON) == true) {
        //     val byteArray = bundle.getByteArray(NotificationCompat.EXTRA_LARGE_ICON)
        //     Log.d(TAG, "onNotificationPosted: $byteArray")
        // val bStream = ByteArrayOutputStream()
        // bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream)
        // val byteArray = bStream.toByteArray()
        //     broadcastIntent.putExtra("image", byteArray)
        //     broadcastIntent.putExtra("isImage", true)
        // } else {
        broadcastIntent.putExtra("message", message)
        broadcastIntent.putExtra("isImage", false)
        //}

        broadcastIntent.setClass(this, Restarter::class.java)
        this.sendBroadcast(broadcastIntent)
    }

    companion object {
        const val TAG = "NotificationListener"
        private const val WA_PACKAGE = "com.whatsapp"
        var notification: Notification? = null
    }
}