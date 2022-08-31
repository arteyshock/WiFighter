package com.artsavin.wifighter

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.wear.tiles.TileService
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester

class WifiActivity: ComponentActivity() {

    override fun onStart() {
        super.onStart()
        startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        // updating complication
        val complicationDataSourceUpdateRequester =
            ComplicationDataSourceUpdateRequester.create(
                applicationContext,
                ComponentName(
                    applicationContext,
                    WifiComplicationService::class.java
                )
            )
        complicationDataSourceUpdateRequester.requestUpdateAll()
        // Updating tile
        TileService.getUpdater(applicationContext)
            .requestUpdate(WifiTileService::class.java)
        finish()
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, WifiActivity::class.java)
    }
}