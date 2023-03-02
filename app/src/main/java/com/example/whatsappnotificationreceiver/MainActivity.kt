package com.example.whatsappnotificationreceiver

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappnotificationreceiver.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    companion object {
        var myMsgList = ArrayList<ModelClass>()
        var rvMsg: RecyclerView? = null
    }

    lateinit var binding: ActivityMainBinding
    private var mYourService: MyService? = null
    var mServiceIntent: Intent? = null

    private var ENABLED_NOTIFICATION_LISTENERS = "ENABLED_NOTIFICATION_LISTENERS"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvMsg = binding.rvMsgs
        rvMsg?.adapter = WhatsappNotificationAdapter(myMsgList,this)

        mYourService = MyService()
        mServiceIntent = Intent(this, mYourService?.javaClass)
        if (!mYourService?.javaClass?.let { isMyServiceRunning(it) }!!) {
            startService(mServiceIntent)
        }

        binding.btnClear.setOnClickListener {
            myMsgList.clear()
            rvMsg?.adapter = WhatsappNotificationAdapter(myMsgList,this)
            rvMsg?.adapter?.notifyDataSetChanged()
        }

        binding.btnCheckPermission.setOnClickListener {
            startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.i("Service status", "Running")
                return true
            }
        }
        Log.i("Service status", "Not running")
        return false
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName = packageName
        val flat: String = Settings.Secure.getString(
            contentResolver,
            ENABLED_NOTIFICATION_LISTENERS
        )
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":".toRegex()).toTypedArray()
            for (name in names) {
                val cn = ComponentName.unflattenFromString(name)
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }
}
