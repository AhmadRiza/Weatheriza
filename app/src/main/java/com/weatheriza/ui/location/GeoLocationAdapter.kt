package com.weatheriza.ui.location

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.weatheriza.data.model.GeoLocation
import com.weatheriza.databinding.ItemGeolocationBinding
import com.weatheriza.databinding.ItemLoadingBinding
import com.weatheriza.ui.location.state.GeoLocationItemModel

class GeoLocationAdapter(
    private val onViewEvent: (GeoLocationAdapterEvent) -> Unit
) : ListAdapter<GeoLocationItemModel, GeoLocationViewHolder>(
    buildItemDiffer()
) {
    enum class ItemType {
        GEO_LOCATION, LOADING
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemType.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeoLocationViewHolder {
        return when (ItemType.values()[viewType]) {
            ItemType.GEO_LOCATION -> {
                GeoLocationViewHolder.GeoLocation(
                    ItemGeolocationBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }

            ItemType.LOADING -> {
                GeoLocationViewHolder.Loading(
                    ItemLoadingBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: GeoLocationViewHolder, position: Int) {
        when (holder) {
            is GeoLocationViewHolder.GeoLocation ->
                holder.bind(getItem(position), onViewEvent)

            is GeoLocationViewHolder.Loading -> {}
        }
    }

}

sealed class GeoLocationViewHolder(itemView: View) : ViewHolder(itemView) {
    class GeoLocation(
        private val binding: ItemGeolocationBinding
    ) : GeoLocationViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: GeoLocationItemModel, onViewEvent: (GeoLocationAdapterEvent) -> Unit) {
            val safeModel = (model as? GeoLocationItemModel.GeoLocationItem) ?: return
            with(binding) {
                textCityName.text = "${safeModel.geoLocation.name},"
                textCountryCode.text = safeModel.geoLocation.countryCode
                imageIconfavorite.isVisible = safeModel.isFavorite
                root.setOnClickListener {
                    onViewEvent(
                        GeoLocationAdapterEvent.OnGeoLocationItemClicked(safeModel.geoLocation)
                    )
                }
            }
        }
    }

    class Loading(binding: ItemLoadingBinding) : GeoLocationViewHolder(binding.root)
}


sealed interface GeoLocationAdapterEvent {
    data class OnGeoLocationItemClicked(val geoLocation: GeoLocation) : GeoLocationAdapterEvent
}

private fun buildItemDiffer() = AsyncDifferConfig.Builder(
    object : DiffUtil.ItemCallback<GeoLocationItemModel>() {
        override fun areItemsTheSame(
            oldItem: GeoLocationItemModel,
            newItem: GeoLocationItemModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GeoLocationItemModel,
            newItem: GeoLocationItemModel
        ): Boolean {
            return oldItem == newItem
        }

    }).build()

