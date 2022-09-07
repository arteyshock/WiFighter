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
        delay(WifiState.REFRESH_TIMEOUT)
        return when (request.complicationType) {
            ComplicationType.SMALL_IMAGE -> {
                SmallImageComplicationData.Builder(
                    smallWifiImage(),
                    PlainComplicationText.Builder(getString(R.string.state_description)).build()
                )
                    .setTapAction(WifiActivity.newPendingIntent(applicationContext))
                    .build()
            }
            ComplicationType.LONG_TEXT -> {
                LongTextComplicationData.Builder(
                    PlainComplicationText.Builder(" IP:  ${wifiState.getIP()}").build(),
                    PlainComplicationText.Builder(getString(R.string.state_description)).build()
                )
                    .setSmallImage(smallWifiImage())
                    .setTapAction(WifiActivity.newPendingIntent(applicationContext))
                    .build()
            }
            else -> null
        }
    }

    private fun getWifiIcon(): Int = if (wifiState.enabled())
        R.drawable.wifi_on_bright else R.drawable.wifi_off

    private fun smallWifiImage(): SmallImage = SmallImage.Builder(
        Icon.createWithResource(applicationContext, getWifiIcon()),
        SmallImageType.ICON
    )
        .build()
}