package com.artsavin.wifighter

import android.graphics.drawable.Icon
import android.util.Log
import androidx.wear.watchface.complications.data.*
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService

class WifiComplicationService: SuspendingComplicationDataSourceService() {

    private val wifiStateManager by lazy {
        WifiSwitcher(application)
    }

    override fun onComplicationActivated(
        complicationInstanceId: Int,
        type: ComplicationType
    ) {
        Log.d("MY_TAG", "onComplicationActivated $this")
    }

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        TODO("Not yet implemented")
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {

        //return
        return when (request.complicationType) {
            ComplicationType.SMALL_IMAGE -> SmallImageComplicationData.Builder(

                SmallImage.Builder(
                    Icon.createWithResource(applicationContext, getWifiIcon()),
                    SmallImageType.ICON
                ).build(),
                PlainComplicationText.Builder(text = "WIFI state").build()
            )
                .setTapAction(null)
                .build()
            else -> null
        }
    }

    private fun getWifiIcon(): Int = if (wifiStateManager.enabled) R.drawable.wifi_on else R.drawable.wifi_off
}