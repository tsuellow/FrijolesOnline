package com.ccy.apps.frijolesonline.data.repository

import android.util.Log
import com.ccy.apps.frijolesonline.common.Resource
import com.ccy.apps.frijolesonline.data.remote.FirebaseDatabaseConnection
import com.ccy.apps.frijolesonline.data.remote.dto.FrijolDto
import com.ccy.apps.frijolesonline.data.remote.dto.LotDto
import com.ccy.apps.frijolesonline.domain.model.*
import com.ccy.apps.frijolesonline.domain.repository.FrijolesRepository
import kotlinx.coroutines.flow.Flow

class FrijolesRepositoryImpl constructor(private val firebaseDatabaseConnection: FirebaseDatabaseConnection) :
    FrijolesRepository {


    override suspend fun getCurrentLotList(): Resource<List<Lot>> {
        return when(val res=firebaseDatabaseConnection.getFrijolesLots()){
            is Resource.Success -> {
                Log.d("testiando34",res.message?:"nda")
                Resource.Success(data= res.data?.map{ it.value.toLot() }?.toList())
            }
            else ->{
                Resource.Error(message = res.message)
            }
        }
    }

    override suspend fun postFrijolLot(lot: Lot): Resource<String> {
        return firebaseDatabaseConnection.postFrijolLot(lot.toLotDto())
    }

    override fun getLot(root: String, pushId: String): Flow<Resource<Lot>> {
        return firebaseDatabaseConnection.getLot(root,pushId)
    }

    override suspend fun postOffer(userId: String, lotId: String, offer: Offer): Boolean {
        return firebaseDatabaseConnection.postOffer(userId,lotId,offer.toOfferDto())
    }

    override suspend fun postReview(userId: String, review: Review): Boolean {
        return firebaseDatabaseConnection.postReview(userId,review.toReviewDto())
    }

    override suspend fun registerUser(userData: UserData): Resource<String> {
        return firebaseDatabaseConnection.registerUser(userData.toUserDataDto())
    }

    override suspend fun updateTransaction(transaction: Transaction): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getPricingData(root: String):Resource<PricingData> {
        return when(val res=firebaseDatabaseConnection.getPricingVars(root)){
            is Resource.Success -> Resource.Success(data = res.data?.toPricingData()?:PricingData())
            is Resource.Error -> Resource.Error(message = res.message)
            is Resource.Loading -> Resource.Loading()
        }
    }
}