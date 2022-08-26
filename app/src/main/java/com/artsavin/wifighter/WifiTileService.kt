package com.artsavin.wifighter

import android.util.Log
import androidx.wear.tiles.DeviceParametersBuilders.DeviceParameters
import androidx.wear.tiles.DimensionBuilders.dp
import androidx.wear.tiles.LayoutElementBuilders.*
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.ResourceBuilders.*
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.TileService
import androidx.wear.tiles.TimelineBuilders.Timeline
import androidx.wear.tiles.TimelineBuilders.TimelineEntry
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.guava.future


class WifiTileService: TileService() {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val wifiSwitcher by lazy {
        WifiSwitcher(application)
    }

    override fun onCreate() {
        super.onCreate()
        //TODO разобраться с жизненным циклом
        getUpdater(application)
            .requestUpdate(WifiTileService::class.java)
        Log.d("MY_TAG", "onCreate $this")
    }

    override fun onTileRequest(requestParams: RequestBuilders.TileRequest): ListenableFuture<Tile> {

        val tileLayout = setupLayout(requestParams.deviceParameters!!)
        Log.d("MY_TAG", "onTileRequest $this")
        return scope.future {
            oneTimeLineEntryTile(tileLayout)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        Log.d("MY_TAG", "onDestroy $this")
    }

    override fun onResourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest
    ): ListenableFuture<Resources> = scope.future {
        Log.d("MY_TAG", "onResourcesRequest $this")
        Resources.Builder()
            .setVersion(RESOURCE_VERSION)
            .addIdToImageMapping(
                ID_IMAGE_WIFI_ON,
                ImageResource.Builder()
                    .setAndroidResourceByResId(
                        AndroidImageResourceByResId.Builder()
                            .setResourceId(R.drawable.wifi_on)
                            .build()
                    )
                    .build()
            )
            .addIdToImageMapping(
                ID_IMAGE_WIFI_OFF,
                ImageResource.Builder()
                    .setAndroidResourceByResId(
                        AndroidImageResourceByResId.Builder()
                            .setResourceId(R.drawable.wifi_off)
                            .build()
                    )
                    .build()
            )
            .build()
    }

    private fun oneTimeLineEntryTile(layout: Layout): Tile = Tile.Builder()
        .setResourcesVersion(RESOURCE_VERSION)
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


    private fun setupLayout(deviceParams: DeviceParameters): Layout {

        return Layout.Builder().setRoot(
            Column.Builder()
                .addContent(
                    setWifiButton()
                )
                // Name of connected network
                .addContent(
                    Text.Builder()
                        .setFontStyle(
                            FontStyles.title3(deviceParams).build()
                        )
                        //TODO длинные названия
                        .setText(wifiSwitcher.connectedSsid)
                        .build()
                )
                .build()
        )
        .build()
    }

    private fun setWifiButton() = Image.Builder()
        .setWidth(BUTTON_SIZE)
        .setHeight(BUTTON_SIZE)
        .setResourceId(setImage())
        .build()

    private fun setImage(): String {
        return if (wifiSwitcher.enabled) {
            ID_IMAGE_WIFI_ON
        } else {
            ID_IMAGE_WIFI_OFF
        }
    }

    companion object {
        private const val RESOURCE_VERSION = "1337"
        private const val ID_IMAGE_WIFI_ON = "wifi_on"
        private const val ID_IMAGE_WIFI_OFF = "wifi_off"
        private val BUTTON_SIZE = dp(42f)
    }
}