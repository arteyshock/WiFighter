package com.artsavin.wifighter

import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager

class WifiState(private val context: Context) {

    fun enabled(): Boolean {
        val manager = context.getSystemService(WIFI_SERVICE) as WifiManager
        return manager.wifiState in setOf(
            WifiManager.WIFI_STATE_ENABLED,
            WifiManager.WIFI_STATE_ENABLING
        )
    }
}