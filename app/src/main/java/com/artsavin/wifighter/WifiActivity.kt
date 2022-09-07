package com.artsavin.wifighter

import android.app.PendingIntent
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
        private const val REQUEST_CODE = 1337

        fun newIntent(context: Context) = Intent(context, WifiActivity::class.java)

        fun newPendingIntent(context: Context): PendingIntent = PendingIntent
            .getActivity(
                context,
                REQUEST_CODE,
                Intent(context, WifiActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
    }
}