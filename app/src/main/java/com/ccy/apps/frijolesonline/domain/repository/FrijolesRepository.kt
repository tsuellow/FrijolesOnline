package com.ccy.apps.frijolesonline.domain.repository

import com.ccy.apps.frijolesonline.common.Resource
import com.ccy.apps.frijolesonline.domain.model.*
import kotlinx.coroutines.flow.Flow

interface FrijolesRepository {

    suspend fun getCurrentLotList():Resource<List<Lot>>

    suspend fun postFrijolLot(lot:Lot):Resource<String>

    fun getLot(root:String, pushId:String): Flow<Resource<Lot>>

    suspend fun postOffer(userId:String, lotId:String, offer: Offer):Boolean

    suspend fun postReview(userId:String, review: Review):Boolean

    //returns userId
    suspend fun registerUser(userData:UserData):Resource<String>

    suspend fun updateTransaction(transaction: Transaction):Boolean

    suspend fun getPricingData(root:String):Resource<PricingData>


}