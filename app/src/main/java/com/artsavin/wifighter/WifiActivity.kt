package com.artsavin.wifighter

import android.content.Intent
import android.provider.Settings
import androidx.activity.ComponentActivity

class WifiActivity: ComponentActivity() {
    /**
     *  Just starts WiFi settings and leaves
     */
    override fun onStart() {
        super.onStart()
        startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        finish()
    }
}