package com.ccy.apps.frijolesonline.data.remote.dto

import com.ccy.apps.frijolesonline.domain.model.Produce
import com.ccy.apps.frijolesonline.domain.model.Transaction

data class TransactionDto(
    val lot:LotDto = LotDto(),
    val offers:Map<String,OfferDto> = emptyMap(),
    val status:String="new",
    val statusDate:String="01.01.2022",
    val statusHistory:Map<String,String> = emptyMap(),
    val deliveryAddress:String="",
    val deliveryDate:String=""
){
    fun toTransaction():Transaction{
        return Transaction(
            lot = lot.toLot(),
            offers = HashMap(offers.mapValues { it.value.toOffer() }),
            status = status,
            statusDate = statusDate,
            statusHistory = statusHistory,
            deliveryAddress = deliveryAddress,
            deliveryDate = deliveryDate,
        )
    }
}
