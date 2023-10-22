package com.weatheriza.ui.location.usecase

import com.weatheriza.core.base.BaseUseCase
import com.weatheriza.core.model.Result
import com.weatheriza.data.repository.OpenWeatherRepository
import com.weatheriza.ui.location.state.GeoLocationItemModel
import com.weatheriza.ui.location.state.SearchDisplayState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchLocationUseCase @Inject constructor(
    private val repository: OpenWeatherRepository
) : BaseUseCase<Flow<SearchDisplayState>, String>() {
    override suspend fun build(params: String?): Flow<SearchDisplayState> {
        requireNotNull(params)
        return flow {
            if (params.length < 3) {
                val favorites = repository.getAllFavoriteCity().map {
                    GeoLocationItemModel.GeoLocationItem(it, true)
                }
                emit(SearchDisplayState.DisplayLocationList(favorites))
                return@flow
            }
            emit(SearchDisplayState.DisplayLocationList(listOf(GeoLocationItemModel.Loading())))
            when (val result = repository.searchGeoLocation(params)) {
                is Result.Error -> emit(SearchDisplayState.Error(result.errorMessage))
                Result.Success.Empty -> if (params.isEmpty()) {
                    emit(SearchDisplayState.Empty)
                } else {
                    emit(SearchDisplayState.LocationNotFound)
                }

                is Result.Success.WithData -> SearchDisplayState.DisplayLocationList(
                    result.data.map { GeoLocationItemModel.GeoLocationItem(it, false) }
                ).let { state -> emit(state) }
            }
        }

    }

}