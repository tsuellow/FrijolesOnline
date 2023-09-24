package com.ccy.apps.frijolesonline.presentation.lot_list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccy.apps.frijolesonline.common.*
import com.ccy.apps.frijolesonline.domain.model.Lot
import com.ccy.apps.frijolesonline.domain.use_cases.lot_list_use_cases.LotListUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class LotListViewModel @Inject constructor(
    private val allUseCases: LotListUseCases
) : ViewModel() {

    var filterArray: SnapshotStateMap<String, PropertyClass>
        private set

    var lotOrderCriteria: SnapshotStateMap<String, PropertyClass>
        private set

    var lotOrdering = mutableStateOf(Lot.Features.Property.DateUpdated.key)

    var orderType: MutableState<OrderType> = mutableStateOf(OrderType.Descending)

    var lotList = mutableStateOf<Resource<List<Lot>>>(Resource.Loading())
        private set

    var filteredLotList = mutableStateOf<Resource<List<Lot>>>(Resource.Loading())
        private set

    init {
        filterArray = allUseCases.getFilterArrayUseCase("frijol").map { Pair(it.key, it.value) }
            .toMutableStateMap()
        lotOrderCriteria = allUseCases.getOrderingsUseCase("frijol").map { Pair(it.key, it.value) }
            .toMutableStateMap()
        fetchList()
    }

    private fun fetchList() {
        allUseCases.getLotsUseCase(
            filterArray = HashMap(filterArray.mapValues { it.value.propertyType }),
            order = lotOrdering.value
        ).onEach { res ->

            lotList.value = res
            filterLotList()
        }.launchIn(viewModelScope)
    }

    private fun filterLotList() {
        filteredLotList.value = when(val rawResponse=lotList.value){
            is Resource.Loading ->{
                Resource.Loading(data = filteredLotList.value.data)
            }
            is Resource.Success ->{
                Resource.Success(data = lotList.value.data?.let { rawList ->
                    allUseCases.reorderAndFilterUseCase(
                        rawList = rawList,
                        filterArray = HashMap(filterArray.mapValues { it.value.propertyType }),
                        order = lotOrdering.value,
                        orderType = orderType.value
                    ) }?: emptyList())
            }
            else ->{
            lotList.value}
        }
    }

    fun areFiltersActive():Boolean{
        filterArray.forEach{(key,value)->
            if (value.propertyType.isActive()){
                return true
            }
        }
        return false
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.FilterEvent -> {
                filterArray[event.filterAction.first] =
                    filterArray[event.filterAction.first]!!.copy(propertyType = event.filterAction.second)
                filterLotList()
            }
            is Event.RemoveAllFilters -> {
                filterArray.forEach {(key,value) ->
                    filterArray[key] = value.copy(propertyType = value.propertyType.removeFilter())
                }
                filterLotList()
            }
            is Event.RefreshEvent -> fetchList()
            is Event.ReorderEvent -> {
                if (lotOrdering.value.contentEquals(event.lotOrder)) {
                    orderType.value = if (orderType.value is OrderType.Ascending) {
                        OrderType.Descending
                    } else {
                        OrderType.Ascending
                    }
                }
                lotOrdering.value = event.lotOrder
                filterLotList()
            }
        }
    }

}

sealed class Event {
    data class ReorderEvent(val lotOrder: String) : Event()
    data class FilterEvent(val filterAction: Pair<String, PropertyType>) : Event()
    object RefreshEvent : Event()
    object RemoveAllFilters : Event()
}