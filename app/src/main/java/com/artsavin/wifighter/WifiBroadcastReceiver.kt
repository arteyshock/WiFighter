package com.artsavin.wifighter

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class WifiBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        context.startActivity(WifiActivity.newIntent(context))
    }

    companion object {

        private const val REQUEST_CODE = 1337

        fun broadcast(context: Context): PendingIntent = PendingIntent
            .getBroadcast(
                context,
                REQUEST_CODE,
                Intent(context, WifiBroadcastReceiver::class.java),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
    }
}