package com.artsavin.wifighter

import android.util.Log
import androidx.wear.tiles.*
import androidx.wear.tiles.DeviceParametersBuilders.*
import androidx.wear.tiles.DimensionBuilders.expand
import androidx.wear.tiles.LayoutElementBuilders.*
import androidx.wear.tiles.ResourceBuilders.*
import androidx.wear.tiles.TileBuilders.*
import androidx.wear.tiles.TimelineBuilders.*
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.guava.future


class WifiTileService: TileService() {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val wifiSwitcher by lazy {
        WifiSwitcher(application)
    }

    override fun onTileRequest(requestParams: RequestBuilders.TileRequest): ListenableFuture<Tile> {

        val tileLayout = setupLayout(requestParams.deviceParameters!!)

        return scope.future {
            oneTimeLineEntryTile(tileLayout)
        }
    }

    override fun onResourcesRequest(
        requestParams: RequestBuilders.ResourcesRequest
    ): ListenableFuture<Resources> = scope.future {

        Resources.Builder()
            .setVersion(RESOURCE_VERSION)
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
                // Button for switching WiFi
                    // TODO поменять на кнопку
                .addContent(
                    Text.Builder()
                        .setFontStyle(
                            FontStyles.title3(deviceParams).build()
                        )
                        .setText(wifiSwitcher.enabled.toString())
                        .build()
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

    companion object {
        private const val RESOURCE_VERSION = "1337"
    }


}