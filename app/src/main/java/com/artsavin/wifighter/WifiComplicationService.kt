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

    override fun getPreviewData(type: ComplicationType): ComplicationData? =
        correctComplicationData(type)

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
        // User have REFRESH_TIMEOUT sec to change wifi state
        delay(WifiState.REFRESH_TIMEOUT)
        return correctComplicationData(request.complicationType)
    }

    private fun correctComplicationData(type: ComplicationType) =
        when (type) {
            ComplicationType.SMALL_IMAGE -> {
                SmallImageComplicationData.Builder(
                    smallWifiImage(),
                    description()
                )
                    .setTapAction(WifiActivity.newPendingIntent(applicationContext))
                    .build()
            }
            ComplicationType.LONG_TEXT -> {
                LongTextComplicationData.Builder(
                    PlainComplicationText.Builder(" IP:  ${wifiState.getIP()}").build(),
                    description()
                )
                    .setSmallImage(smallWifiImage())
                    .setTapAction(WifiActivity.newPendingIntent(applicationContext))
                    .build()
            }
            else -> null
        }

    private fun description() =
        PlainComplicationText.Builder(getString(R.string.state_description)).build()

    private fun getWifiIcon(): Int = if (wifiState.enabled())
        R.drawable.wifi_on_bright else R.drawable.wifi_off

    private fun smallWifiImage(): SmallImage = SmallImage.Builder(
        Icon.createWithResource(applicationContext, getWifiIcon()),
        SmallImageType.ICON
    )
        .build()
}