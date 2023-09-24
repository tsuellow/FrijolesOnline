package com.ccy.apps.frijolesonline.domain.model

import com.ccy.apps.frijolesonline.data.remote.dto.ProduceDto
import com.ccy.apps.frijolesonline.data.remote.dto.TransactionDto

data class Transaction(
    val lot: Lot = Lot(),
    val offers:Map<String, Offer> = emptyMap(),
    val status:String="new",
    val statusDate:String="01.01.2022",
    val statusHistory:Map<String,String> = emptyMap(),
    val deliveryAddress:String="",
    val deliveryDate:String=""
){
    fun toTransactionDto():TransactionDto{
        return TransactionDto(
            lot = lot.toLotDto(),
            offers = HashMap(offers.mapValues { it.value.toOfferDto() }),
            status = status,
            statusDate = statusDate,
            statusHistory = statusHistory,
            deliveryAddress = deliveryAddress,
            deliveryDate = deliveryDate,
        )
    }
}
