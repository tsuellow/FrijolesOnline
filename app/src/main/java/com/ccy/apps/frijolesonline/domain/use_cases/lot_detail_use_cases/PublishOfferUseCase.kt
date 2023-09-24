package com.ccy.apps.frijolesonline.domain.use_cases.lot_detail_use_cases

import com.ccy.apps.frijolesonline.domain.model.Offer
import com.ccy.apps.frijolesonline.domain.repository.FrijolesRepository

class PublishOfferUseCase(val repository: FrijolesRepository) {
    suspend operator fun invoke(offer:Offer, pushId:String):Boolean{
        return repository.postOffer("testUser", pushId, offer)
    }
}