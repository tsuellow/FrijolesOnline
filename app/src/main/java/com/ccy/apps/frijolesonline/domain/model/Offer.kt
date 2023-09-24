package com.ccy.apps.frijolesonline.domain.model

import com.ccy.apps.frijolesonline.data.remote.dto.OfferDto

data class Offer(
    val userData: UserData = UserData(),
    val price: Int = 0,
    val quantity: Int=0,
    val deliveryPlace: String = "",
    val comment:String=""
){
    companion object{
        const val FINCA ="FINCA"
    }
    fun toOfferDto(): OfferDto {
        return OfferDto(
            userData = userData.toUserDataDto(),
            price = price,
            quantity = quantity,
            deliveryPlace = deliveryPlace,
            comment = comment,
        )
    }
}
