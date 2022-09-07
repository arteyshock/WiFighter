package com.artsavin.wifighter

import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager

class WifiState(private val context: Context) {

    private val manager by lazy {
        context.getSystemService(WIFI_SERVICE) as WifiManager
    }

    fun enabled(): Boolean = manager.wifiState in setOf(
            WifiManager.WIFI_STATE_ENABLED,
            WifiManager.WIFI_STATE_ENABLING
        )

    fun getIP(): String {
        val ip = manager.dhcpInfo.ipAddress
        return if (ip > 0) intIpToString(ip) else " -- . -- . -- . -- "
    }

    private fun intIpToString(ip: Int): String = arrayOf(0, 8, 16, 24)
        .joinToString(separator = ".") {
            ip.shr(it).and(255).toString()
        }

    companion object {
        const val REFRESH_TIMEOUT = 9000L
    }
}