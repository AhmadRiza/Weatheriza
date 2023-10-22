package com.weatheriza.ui.main

import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.weatheriza.databinding.ItemForecastBinding
import com.weatheriza.ui.main.state.ForecastDisplayItemModel

class ForecastAdapter(
    private val onViewEvent: (ForecastAdapterEvent) -> Unit
) : ListAdapter<ForecastDisplayItemModel, ForeCastViewHolder>(
    buildItemDiffer()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForeCastViewHolder {
        return ForeCastViewHolder(
            ItemForecastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ForeCastViewHolder, position: Int) {
        val item = getItem(position)
        holder.onClickListener = OnClickListener {
            onViewEvent(ForecastAdapterEvent.OnForecastClick(item))
        }
        holder.bindModel(item)
    }
}


class ForeCastViewHolder(private val binding: ItemForecastBinding) : ViewHolder(binding.root) {

    lateinit var onClickListener: OnClickListener
    fun bindModel(model: ForecastDisplayItemModel) {
        with(binding) {
            root.setOnClickListener(onClickListener)
            textDate.text = model.dateLabel
            textDayName.text = model.dayLabel
            imageWeatherIcon.load(model.weatherIconUrl)
            textTemp.text = model.temperature
            indicator.isVisible = model.isSelected
            divider.isVisible = !model.isSelected
        }
    }
}

sealed interface ForecastAdapterEvent {
    data class OnForecastClick(val model: ForecastDisplayItemModel) : ForecastAdapterEvent
}

private fun buildItemDiffer() = AsyncDifferConfig.Builder(
    object : DiffUtil.ItemCallback<ForecastDisplayItemModel>() {
        override fun areItemsTheSame(
            oldItem: ForecastDisplayItemModel,
            newItem: ForecastDisplayItemModel
        ): Boolean {
            return oldItem.dateUnix == newItem.dateUnix
        }

        override fun areContentsTheSame(
            oldItem: ForecastDisplayItemModel,
            newItem: ForecastDisplayItemModel
        ): Boolean {
            return oldItem == newItem
        }
    }).build()

