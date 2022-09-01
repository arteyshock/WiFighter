package com.artsavin.wifighter

import android.graphics.drawable.Icon
import androidx.wear.watchface.complications.data.*
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import kotlinx.coroutines.delay

class WifiComplicationService: SuspendingComplicationDataSourceService() {

    private val wifiState by lazy {
        WifiState(applicationContext)
    }

    override fun onComplicationActivated(
        complicationInstanceId: Int,
        type: ComplicationType
    ) {

    }

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        TODO("Not yet implemented")
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
        // User have 3 sec to change wifi state
        delay(3000)
        return when (request.complicationType) {
            ComplicationType.SMALL_IMAGE -> SmallImageComplicationData.Builder(

                SmallImage.Builder(
                    Icon.createWithResource(applicationContext, getWifiIcon()),
                    SmallImageType.ICON
                ).build(),
                PlainComplicationText.Builder(text = "WIFI state").build()
            )
                .setTapAction(WifiBroadcastReceiver.broadcast(applicationContext))
                .build()
            else -> null
        }
    }

    private fun getWifiIcon(): Int = if (wifiState.enabled())
        R.drawable.wifi_on_bright else R.drawable.wifi_off
}