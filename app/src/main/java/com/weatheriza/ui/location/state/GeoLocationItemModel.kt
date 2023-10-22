package com.weatheriza.ui.location.state

import com.weatheriza.data.model.GeoLocation
import com.weatheriza.ui.location.GeoLocationAdapter.ItemType

interface GeoLocationItemModel {
    val id: String
    val itemType: ItemType
    override fun equals(other: Any?): Boolean
    data class GeoLocationItem(
        val geoLocation: GeoLocation,
        val isFavorite: Boolean
    ) : GeoLocationItemModel {
        override val id: String = "item-${geoLocation.name}${geoLocation.longitude}"
        override val itemType: ItemType = ItemType.GEO_LOCATION
    }

    data class Loading(override val id: String = "loading") : GeoLocationItemModel {
        override val itemType: ItemType = ItemType.LOADING
    }
}