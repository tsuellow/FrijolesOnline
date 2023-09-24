package com.ccy.apps.frijolesonline.presentation.lot_detail

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccy.apps.frijolesonline.common.PropertyClass
import com.ccy.apps.frijolesonline.common.PropertyType
import com.ccy.apps.frijolesonline.common.Resource
import com.ccy.apps.frijolesonline.domain.model.Frijol
import com.ccy.apps.frijolesonline.domain.model.Lot
import com.ccy.apps.frijolesonline.domain.model.Offer
import com.ccy.apps.frijolesonline.domain.model.Offer.Companion.FINCA
import com.ccy.apps.frijolesonline.domain.model.PricingData
import com.ccy.apps.frijolesonline.domain.model.Produce
import com.ccy.apps.frijolesonline.domain.use_cases.lot_detail_use_cases.LotDetailUseCases
import com.ccy.apps.frijolesonline.presentation.publish_lot.SubmissionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LotDetailViewModel @Inject constructor(
    val lotDetailUseCases: LotDetailUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var root: String = ""
    var pushId: String = ""

    var lot: Resource<Lot> by mutableStateOf(Resource.Loading())
        private set

    var produce: Produce by mutableStateOf(Frijol())
        private set

    var pricingData: PricingData? = null
        private set

    var offer by mutableStateOf(Offer())
        private set

    var isPriceEditable by mutableStateOf(false)
        private set

    var quantityPropertyClass by
    mutableStateOf(
        PropertyClass(
            name = "quantity",
            description = "Desired Quantity",
            propertyType = PropertyType.IntRange(),
            icon = Icons.Filled.LocalShipping
        )
    )
        private set

    var pricePropertyClass by
    mutableStateOf(
        PropertyClass(
            name = "Price",
            description = "Proposed price",
            propertyType = PropertyType.IntRange(),
            icon = Icons.Filled.RequestQuote
        )
    )
        private set

    var commentPropertyClass by
    mutableStateOf(
        PropertyClass(
            name = "Message",
            description = "Additional info",
            propertyType = PropertyType.FreeText(),
            isRequired = false,
            icon = Icons.Default.Message
        )
    )
        private set

    //var destination: String? by mutableStateOf(null)
       // private set

    var quantityError by mutableStateOf("")
        private set

    var submissionState:SubmissionState by mutableStateOf(SubmissionState.Idle)
        private  set

    var showDialog:Boolean by mutableStateOf(false)
        private set

    init {
        root = savedStateHandle.get<String>("root") ?: "frijol"
        pushId = savedStateHandle.get<String>("pushId") ?: ""

        viewModelScope.launch {
            getPricingData()
            getLotData()
        }

    }

    suspend fun getPricingData() {
        pricingData = lotDetailUseCases.getPricingDataUseCase(root)
    }

    fun getLotData() {
        lotDetailUseCases.getLotDetailUseCase(root, pushId).onEach {
            if (it is Resource.Success) {
                produce = lotDetailUseCases.createProduceFromMapUseCase(
                    produceType = root,
                    map = it.data?.produce ?: HashMap()
                )
                restrictOffer(it.data ?: Lot(), produce)
            }
            lot = it
        }.launchIn(viewModelScope)
    }

    fun restrictOffer(lot: Lot, produce: Produce) {
        offer =
            Offer(price = lot.unitPriceOrigin, quantity = lot.quantity, deliveryPlace = "FINCA")
        //destination = lot.destination
        quantityPropertyClass = quantityPropertyClass.let {
            it.copy(
                propertyType = (it.propertyType as PropertyType.IntRange).copy(
                    minRange=0,
                    maxRange = lot.quantity,
                    unit = produce.getUnits()
                )
            )
        }
        pricePropertyClass = pricePropertyClass.let {
            it.copy(
                propertyType = (it.propertyType as PropertyType.IntRange).copy(unit = "C$/"+produce.getUnits())
            )
        }

    }

    fun isErrorFree():Boolean{
        quantityError=quantityPropertyClass.hasError(""+offer.quantity)
        quantityError.ifEmpty { return true  }
        return false
    }

    fun submitOffer(){
        submissionState=SubmissionState.Processing
        viewModelScope.launch {
            if (lotDetailUseCases.publishOfferUseCase(pushId=pushId, offer = offer)){
                submissionState=SubmissionState.Success("testUser")
            }else{
                submissionState=SubmissionState.Failed(error = "unable to publish")
            }
        }
    }

    fun onEvent(event:LotDetailEvent){
        when(event){
            is LotDetailEvent.EditQuantity -> {
                offer=offer.copy(quantity = event.value)
                if (quantityError.isNotEmpty()){
                    isErrorFree()
                }
            }
            is LotDetailEvent.EditPrice -> {
                offer=offer.copy(price = event.value)
            }
            is LotDetailEvent.EditMsg -> {
                offer=offer.copy(comment = event.value)
            }
            is LotDetailEvent.MakePriceEditable -> {
                isPriceEditable=event.isEditable
                if (!isPriceEditable){
                    offer=offer.copy(price = if (offer.deliveryPlace == FINCA) lot.data?.unitPriceOrigin?:0 else lot.data?.unitPriceDestination?:0)
                }
            }
            is LotDetailEvent.SwitchDeliveryPlace -> {
                offer = if (event.transportedBySeller){
                    quantityPropertyClass = quantityPropertyClass.let {
                        it.copy(
                            propertyType = (it.propertyType as PropertyType.IntRange).copy(
                                minRange= lot.data?.minSale?:0
                            )
                        )
                    }
                    offer.copy(price = lot.data?.unitPriceDestination?:0, deliveryPlace =lot.data?.destination?:FINCA )
                }else{
                    quantityPropertyClass = quantityPropertyClass.let {
                        it.copy(
                            propertyType = (it.propertyType as PropertyType.IntRange).copy(
                                minRange= 0
                            )
                        )
                    }
                    offer.copy(price = lot.data?.unitPriceOrigin?:0, deliveryPlace =FINCA)
                }
            }
            is LotDetailEvent.ShowSubmissionDialog -> {
                showDialog=event.show && isErrorFree()
            }
            LotDetailEvent.SubmitOffer -> {
                submitOffer()
            }
        }
    }



}

sealed class LotDetailEvent(){
    data class EditQuantity(val value:Int):LotDetailEvent()
    data class EditPrice(val value:Int):LotDetailEvent()
    data class EditMsg(val value:String):LotDetailEvent()
    data class SwitchDeliveryPlace(val transportedBySeller:Boolean):LotDetailEvent()
    data class MakePriceEditable(val isEditable:Boolean):LotDetailEvent()
    data class ShowSubmissionDialog(val show:Boolean):LotDetailEvent()
    object SubmitOffer:LotDetailEvent()

}