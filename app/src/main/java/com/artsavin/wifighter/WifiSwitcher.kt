package com.artsavin.wifighter

import android.app.Application
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.util.Log

class WifiSwitcher(private val application: Application) {

    private val manager by lazy {
        application.getSystemService(WIFI_SERVICE) as WifiManager
    }

    var enabled: Boolean = enabled()
        private set
        get() = enabled()


    var connectedSsid: String = "--"
        private set
        get() = if (manager.connectionInfo.ssid == WifiManager.UNKNOWN_SSID) "Разрулить в манифесте пермишены"
        else manager.connectionInfo.ssid


    fun toggle() {
        enabled = !enabled
        manager.isWifiEnabled = enabled
    }

    private fun enabled(): Boolean = manager.wifiState in setOf(
        WifiManager.WIFI_STATE_ENABLED,
        WifiManager.WIFI_STATE_ENABLING
    )

}