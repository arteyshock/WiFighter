package com.artsavin.wifighter

import androidx.core.content.ContextCompat
import androidx.wear.tiles.ActionBuilders
import androidx.wear.tiles.ColorBuilders.ColorProp
import androidx.wear.tiles.ColorBuilders.argb
import androidx.wear.tiles.DeviceParametersBuilders
import androidx.wear.tiles.DimensionBuilders.dp
import androidx.wear.tiles.LayoutElementBuilders.*
import androidx.wear.tiles.ModifiersBuilders.*
import androidx.wear.tiles.RequestBuilders.ResourcesRequest
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.ResourceBuilders.*
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.TileService
import androidx.wear.tiles.TimelineBuilders.Timeline
import androidx.wear.tiles.TimelineBuilders.TimelineEntry
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.guava.future


class WifiTileService: TileService() {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val wifiState by lazy {
        WifiState(applicationContext)
    }


    override fun onTileRequest(requestParams: TileRequest): ListenableFuture<Tile> = scope.future {
        // if request was from our button - open WIFI settings
        if (requestParams.state?.lastClickableId == ID_TOGGLE_WIFI) {
            startActivity(WifiActivity.newIntent(applicationContext))
        }
        if (requestParams.state?.lastClickableId != ID_REFRESH_IP) {
            // User have <REFRESH_TIMEOUT> sec to change wifi state
            delay(WifiState.REFRESH_TIMEOUT)
        }
        val deviceParams = requestParams.deviceParameters!!
        val tileLayout = setupLayout(deviceParams)
        oneTimeLineEntryTile(tileLayout)
    }


    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }


    override fun onResourcesRequest(
        requestParams: ResourcesRequest
    ): ListenableFuture<Resources> = scope.future {

        Resources.Builder()
            .setVersion(RESOURCE_VERSION)
            .addIdToImageMapping(
                ID_IMAGE_WIFI_ON,
                imageResource(R.drawable.wifi_on)
            )
            .addIdToImageMapping(
                ID_IMAGE_WIFI_OFF,
                imageResource(R.drawable.wifi_off)
            )
            .build()
    }


    private fun imageResource(resourceId: Int) = ImageResource.Builder()
        .setAndroidResourceByResId(
            AndroidImageResourceByResId.Builder()
                .setResourceId(resourceId)
                .build()
        )
        .build()


    private fun oneTimeLineEntryTile(layout: Layout): Tile = Tile.Builder()
        .setResourcesVersion(RESOURCE_VERSION)
        .setFreshnessIntervalMillis(0)
        .setTimeline(
            Timeline.Builder()
                .addTimelineEntry(
                    TimelineEntry.Builder()
                        .setLayout(layout)
                        .build()
                )
                .build()
        )
        .build()


    private fun setupLayout(deviceParams: DeviceParametersBuilders.DeviceParameters): Layout = Layout.Builder()
        .setRoot(
            Column.Builder()
                .addContent(
                    setWifiButton()
                )
                .addContent(
                    Spacer.Builder().setHeight(SPACER_HEIGHT).build()
                )
                .addContent(
                    setIpAddressText(deviceParams)
                )
                .build()
        )
        .build()


    private fun setIpAddressText(deviceParams: DeviceParametersBuilders.DeviceParameters): Text = Text.Builder()
        .setFontStyle(
            FontStyles.title3(deviceParams)
                .setColor(ipTextColor())
                .build()
        )
        .setText(wifiState.getIP())
        .setModifiers(
            Modifiers.Builder()
                .setClickable(
                    Clickable.Builder()
                        .setId(ID_REFRESH_IP)
                        .setOnClick(
                            ActionBuilders.LoadAction.Builder().build()
                        )
                        .build()
                )
                .build()
        )
        .build()


    private fun setWifiButton(): Image = Image.Builder()
        .setWidth(BUTTON_SIZE)
        .setHeight(BUTTON_SIZE)
        .setResourceId(setWifiButtonImageId())
        .setModifiers(
            Modifiers.Builder()
                .setPadding(
                    Padding.Builder()
                        .setAll(BUTTON_PADDING)
                        .build()
                )
                // Задаем задний фон
                .setBackground(
                    Background.Builder()
                        // Скругления
                        .setCorner(Corner.Builder().setRadius(BUTTON_RADIUS).build())
                        // Цвет
                        .setColor(
                            argb(ContextCompat.getColor(this, R.color.dark))
                        )
                        .build()
                )
                .setClickable(
                    Clickable.Builder()
                        .setId(ID_TOGGLE_WIFI)
                        .setOnClick(
                            ActionBuilders.LoadAction.Builder().build()
                        )
                        .build()
                )
                .build()
        )
        .build()

    private fun ipTextColor(): ColorProp {
        val color_id = if (wifiState.enabled()) R.color.color_on_bright else R.color.color_off
        return ColorProp.Builder()
            .setArgb(getColor(color_id))
            .build()
    }



    private fun setWifiButtonImageId(): String = if (wifiState.enabled())
        ID_IMAGE_WIFI_ON else ID_IMAGE_WIFI_OFF


    companion object {
        private const val RESOURCE_VERSION = "1337"
        private const val ID_IMAGE_WIFI_ON = "wifi_on"
        private const val ID_IMAGE_WIFI_OFF = "wifi_off"
        private const val ID_TOGGLE_WIFI = "toggle_wifi"
        private const val ID_REFRESH_IP = "refresh_ip"
        private const val REFRESH_TIMEOUT = 10000L

        private val BUTTON_SIZE = dp(72f)
        private val BUTTON_RADIUS = dp(36f)
        private val BUTTON_PADDING = dp(8f)
        private val SPACER_HEIGHT = dp(12f)
    }
}