package com.ccy.apps.frijolesonline.data.remote.dto

import com.ccy.apps.frijolesonline.domain.model.Offer

data class OfferDto(
    val userData: UserDataDto = UserDataDto(),
    val price: Int = 0,
    val quantity: Int = 0,
    val deliveryPlace: String = "",
    val comment: String = ""
) {
    fun toOffer(): Offer {
        return Offer(
            userData = userData.toUserData(),
            price = price,
            quantity = quantity,
            deliveryPlace = deliveryPlace,
            comment = comment,
        )
    }
}
