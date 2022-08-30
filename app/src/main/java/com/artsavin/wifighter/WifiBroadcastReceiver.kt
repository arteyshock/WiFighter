package com.artsavin.wifighter

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.util.Log
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester

class WifiBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("MY_TAG", "onReceive $this")
        // Get wifi state
        val extras = intent?.extras ?: return
        val complicationId = extras.getInt(WIFI_COMPLICATION_ID)
        val info = extras.getParcelable<WifiInfo>(WifiManager.EXTRA_NETWORK_INFO) ?: return
        // Set icon to Complication
            //TODO
        // create requestUpdate
        val complicationDataSourceUpdateRequester =
            ComplicationDataSourceUpdateRequester.create(
                context = context!!,
                complicationDataSourceComponent = ComponentName("com.artsavin.wifighter", "WifiComplicationService" )
            )
        complicationDataSourceUpdateRequester.requestUpdateAll()

        //TODO update Tile
    }

    companion object {
        private const val WIFI_COMPLICATION_ID = "wifi_complication_id"
    }
}