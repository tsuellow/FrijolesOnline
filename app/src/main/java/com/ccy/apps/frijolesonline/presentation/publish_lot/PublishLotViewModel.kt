package com.ccy.apps.frijolesonline.presentation.publish_lot

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle.Delegate
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.ccy.apps.frijolesonline.common.PropertyClass
import com.ccy.apps.frijolesonline.common.PropertyType
import com.ccy.apps.frijolesonline.common.Resource
import com.ccy.apps.frijolesonline.domain.model.Frijol
import com.ccy.apps.frijolesonline.domain.model.Lot
import com.ccy.apps.frijolesonline.domain.model.Produce
import com.ccy.apps.frijolesonline.domain.use_cases.publish_lot_use_cases.PublishLotUseCases
import com.ccy.apps.frijolesonline.presentation.publish_lot.components.PublishScreen
import com.google.android.gms.common.data.DataBufferObserver.Observable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

@RequiresApi(Build.VERSION_CODES.N)
@HiltViewModel
class PublishLotViewModel @Inject constructor(
    val publishLotUseCases: PublishLotUseCases,
    val navController: NavHostController
) : ViewModel() {

    private val produceType: String = Frijol.categoryName

    val propertyMap = publishLotUseCases.getProducePropertyMapUseCase(Frijol.categoryName)

    val produceMap: SnapshotStateMap<String, Any?> =
        propertyMap.map { Pair(it.key, null) }.toMutableStateMap()

    val produceErrorMap: SnapshotStateMap<String, String> =
        propertyMap.map { Pair(it.key, "") }.toMutableStateMap()

    lateinit var produce: Produce

    var checkInputsProduce= mutableStateOf(false)
        private set

    var produceCompleted = mutableStateOf(false)
        private set

    var lotCompleted = mutableStateOf(false)
        private set

    var lot = mutableStateOf(Lot())
        private set

    lateinit var lotPropertyMap:LinkedHashMap<String,PropertyClass>

    val lotMap: SnapshotStateMap<String, Any?> =
        Lot.getEditables(units = "units").map { Pair(it.key, null) }.toMutableStateMap()

    val lotErrorMap: SnapshotStateMap<String, String> =
        Lot.getEditables(units = "units").map { Pair(it.key, "") }.toMutableStateMap()

    var isLotDeliverable = mutableStateOf(false)
        private set

    var checkInputsLot= mutableStateOf(false)
        private set

    var stateFlow:MutableStateFlow<String> = MutableStateFlow("")

    private val _snackbarState: MutableSharedFlow<String> = MutableSharedFlow()
    val snackbarState: SharedFlow<String> =_snackbarState



    var submissionState:MutableState<SubmissionState> = mutableStateOf(SubmissionState.Idle)
        private set


    private fun digestProduceCreation() {
        checkInputsProduce.value=true

        if (isProduceErrorFree()) {
            produce=publishLotUseCases.createProduceFromMapUseCase.invoke(
                produceType,
                HashMap(produceMap)
            )
            produceMap.putAll(
                produce.toLinkedHashMap()
            )
            lotPropertyMap=Lot.getEditables(units = produce.getUnits())
            navController.navigate(PublishScreen.LotScreen.route) {
                popUpTo(
                    PublishScreen.LotScreen.route
                ) { inclusive = true }
            }
        } else {
            viewModelScope.launch {
                _snackbarState.emit("Produce data have to be completed first!")
            }
        }
    }

    private fun isProduceErrorFree(): Boolean {
        propertyMap.forEach { (key, property) ->
            produceErrorMap[key]= property.hasError(produceMap[key]?.toString() ?: "")
        }
        produceCompleted.value = produceErrorMap.toMap().values.stream()
            .allMatch(String::isEmpty)
        return produceCompleted.value
    }

    private fun isLotErrorFree(): Boolean {
        lotPropertyMap.forEach { (key, property) ->
            lotErrorMap[key]= property.hasError(lotMap[key]?.toString() ?: "")
        }
        lotCompleted.value = lotErrorMap.toMap().values.stream()
            .allMatch(String::isEmpty)
        return lotCompleted.value
    }

    private fun digestLotCreation() {
        checkInputsLot.value=true

        if (isLotErrorFree()) {
            lot.value = Lot.createFromMap(HashMap(lotMap), produce = produce)
            navController.navigate(PublishScreen.ReviewScreen.route) {
                popUpTo(
                    PublishScreen.ReviewScreen.route
                ) { inclusive = true }
            }
        }else {
            viewModelScope.launch {
                _snackbarState.emit("Produce and Lot data have to be completed first!")
            }
        }
    }

    private fun submitLot(){
        submissionState.value=SubmissionState.Processing
        viewModelScope.launch{
            when(val res=publishLotUseCases.submitLotUseCase(lot.value.copy(produce = produce.toLinkedHashMap()))){
                is Resource.Error -> submissionState.value=SubmissionState.Failed(error = res.message?:"Unknown Error")
                is Resource.Loading -> submissionState.value=SubmissionState.Processing
                is Resource.Success -> submissionState.value=SubmissionState.Success(pushId = res.data!!)
            }
        }
    }

    fun onEvent(publishLotEvent: PublishLotEvent) {
        when (publishLotEvent) {
            is PublishLotEvent.ProduceSubmittedEvent -> {
                digestProduceCreation()
            }

            is PublishLotEvent.EditProduceMapEvent -> produceMap[publishLotEvent.key] =
                publishLotEvent.value

            is PublishLotEvent.ProduceErrorEvent -> produceErrorMap[publishLotEvent.key] =
                publishLotEvent.error

            is PublishLotEvent.NavigateEvent -> {
                when (val screen = publishLotEvent.screen) {
                    PublishScreen.ProduceScreen -> {
                        navController.navigate(screen.route) {
                            popUpTo(
                                screen.route
                            ) { inclusive = true }
                        }
                    }

                    PublishScreen.LotScreen -> {
                        digestProduceCreation()
                    }

                    PublishScreen.ReviewScreen -> {
                        if (isProduceErrorFree()){
                            digestLotCreation()
                        }else{
                            viewModelScope.launch {
                                _snackbarState.emit("Produce and Lot data have to be completed first!")
                            }
                        }
                    }
                }
            }

            is PublishLotEvent.LotSubmittedEvent -> {
                digestLotCreation()
            }

            is PublishLotEvent.EditLotMapEvent -> {

                when (publishLotEvent.key) {
                    Lot.Features.Property.Quantity.key -> {
                        lotPropertyMap[Lot.Features.Property.MinimumSale.key]?.let {

                            lotPropertyMap[Lot.Features.Property.MinimumSale.key] = it.copy(
                                propertyType = (it.propertyType as PropertyType.IntRange).copy(
                                    maxRange = if (publishLotEvent.value is Int) publishLotEvent.value else 0
                                )
                            )
                        }

                    }
                    Lot.Features.Property.UnitPriceOrigin.key -> {
                        lotPropertyMap[Lot.Features.Property.UnitPriceDestination.key]?.let {
                            lotPropertyMap[Lot.Features.Property.UnitPriceDestination.key] = it.copy(
                                propertyType = (it.propertyType as PropertyType.IntRange).copy(
                                    minRange = if (publishLotEvent.value is Int) publishLotEvent.value else 0
                                )
                            )
                        }
                    }
                }
                lotMap[publishLotEvent.key] = publishLotEvent.value

            }

            is PublishLotEvent.LotErrorEvent -> lotErrorMap[publishLotEvent.key] =
                publishLotEvent.error

            is PublishLotEvent.SetIsDeliverable -> {
                isLotDeliverable.value = publishLotEvent.deliverable
                lotPropertyMap[Lot.Features.Property.Destination.key] =
                    lotPropertyMap[Lot.Features.Property.Destination.key]!!.copy(isRequired = isLotDeliverable.value)
                lotPropertyMap[Lot.Features.Property.UnitPriceDestination.key] =
                    lotPropertyMap[Lot.Features.Property.UnitPriceDestination.key]!!.copy(
                        isRequired = isLotDeliverable.value
                    )
                lotPropertyMap[Lot.Features.Property.MinimumSale.key] =
                    lotPropertyMap[Lot.Features.Property.MinimumSale.key]!!.copy(
                        isRequired = isLotDeliverable.value
                    )
                if (!isLotDeliverable.value) {
                    Log.d("testin", "" + this)
                    lotMap[Lot.Features.Property.Destination.key] = null
                    lotMap[Lot.Features.Property.UnitPriceDestination.key] = null
                    lotMap[Lot.Features.Property.MinimumSale.key] = null

                    lotErrorMap[Lot.Features.Property.Destination.key] = ""
                    lotErrorMap[Lot.Features.Property.UnitPriceDestination.key] = ""
                    lotErrorMap[Lot.Features.Property.MinimumSale.key] = ""
                }
            }

            PublishLotEvent.PublishEvent -> {
                when (submissionState.value){
                    is SubmissionState.Idle, is  SubmissionState.Failed ->{
                        submitLot()
                    }
                    SubmissionState.Processing -> viewModelScope.launch { _snackbarState.emit("is processing") }
                    is SubmissionState.Success -> viewModelScope.launch { _snackbarState.emit("Submission succeeded! exit or tap to view My Lots.") }
                }
            }
        }
    }


}

interface LimitedMutableStateFlow<T>{

    fun compareAndSet(expect: T, update: T): Boolean

    suspend fun collect(collector: FlowCollector<T>): Nothing

    fun resetReplayCache()

    fun tryEmit(value: T): Boolean

}

sealed class PublishLotEvent {
    object LotSubmittedEvent : PublishLotEvent()
    object ProduceSubmittedEvent : PublishLotEvent()
    object PublishEvent : PublishLotEvent()
    data class EditProduceMapEvent(val key: String, val value: Any?) : PublishLotEvent()
    data class ProduceErrorEvent(val key: String, val error: String) : PublishLotEvent()
    data class NavigateEvent(val screen: PublishScreen) : PublishLotEvent()
    data class EditLotMapEvent(val key: String, val value: Any?) : PublishLotEvent()
    data class LotErrorEvent(val key: String, val error: String) : PublishLotEvent()
    data class SetIsDeliverable(val deliverable: Boolean) : PublishLotEvent()
}

sealed class SubmissionState{
    object Idle:SubmissionState()
    object Processing:SubmissionState()
    data class Success(val pushId:String):SubmissionState()
    data class Failed(val error:String):SubmissionState()
}