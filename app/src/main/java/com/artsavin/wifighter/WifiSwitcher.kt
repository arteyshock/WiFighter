package com.artsavin.wifighter

import android.app.Application
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.net.wifi.WifiManager
import android.provider.Settings

class WifiSwitcher(private val application: Application) {

    private val manager by lazy {
        application.getSystemService(WIFI_SERVICE) as WifiManager
    }

    var enabled: Boolean = enabled()
        private set
        get() = enabled()

    private fun enabled(): Boolean = manager.wifiState in setOf(
        WifiManager.WIFI_STATE_ENABLED,
        WifiManager.WIFI_STATE_ENABLING
    )

    fun launchWifiSettings() {
        application.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
    }

    //TODO SSID of active Wifi connection

}