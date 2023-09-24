package com.ccy.apps.frijolesonline.data.remote

import android.util.Log
import com.ccy.apps.frijolesonline.common.Resource
import com.ccy.apps.frijolesonline.data.remote.dto.*
import com.ccy.apps.frijolesonline.domain.model.Lot
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseDatabaseConnection(url:String) {
    private val firebaseDb= Firebase.database(url)

    private val adminRef=firebaseDb.getReference("admin")
    private val transactionRef=firebaseDb.getReference("transactions")
    private val archiveRef=firebaseDb.getReference("archive")
    private val usersRef=firebaseDb.getReference("users")

    private val frijolesTransactions=transactionRef.child("frijol")


    suspend fun getFrijolesLots():Resource<HashMap<String,LotDto>>{

        return when(val response=frijolesTransactions.getEvent()){

            is Resource.Success -> {
                val result= HashMap<String,LotDto>(response.data!!.children.associate { it.key!! to it.getValue(TransactionDto::class.java)!!.lot })
                Resource.Success(result)
            }
            is Resource.Error -> {
                Resource.Error(message = response.message)

            }
            else -> {
                Resource.Error(message = "unexpected error")
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getTransaction(lotId:String):Flow<Resource<TransactionDto>> = flow {
        val ref = frijolesTransactions.child(lotId)
        emit(Resource.Loading<TransactionDto>())
        val event=ref.valueEventFlow()
        event.collect{ res ->
            when(res){
                is Resource.Success -> {
                    val transaction=res.data!!.getValue(TransactionDto::class.java)!!
                    emit(Resource.Success(data = transaction))
                }
                is Resource.Error -> {
                    emit(Resource.Error<TransactionDto>(message = res.message))
                }
                else ->{
                    emit(Resource.Error<TransactionDto>(message = "unexpected error"))
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getLot(root:String, pushId:String):Flow<Resource<Lot>> = flow {
        val ref=transactionRef.child(root).child(pushId)
        emit(Resource.Loading<Lot>())
        val event=ref.valueEventFlow()
        event.collect{ res ->
            when(res){
                is Resource.Success -> {
                    val transaction=res.data!!.getValue(TransactionDto::class.java)!!
                    emit(Resource.Success(data = transaction.lot.toLot()))
                }
                is Resource.Error -> {
                    emit(Resource.Error<Lot>(message = res.message))
                }
                else ->{
                    emit(Resource.Error<Lot>(message = "unexpected error"))
                }
            }
        }
    }



    suspend fun setTransaction(transactionDto: TransactionDto):Boolean{
        return frijolesTransactions.child(transactionDto.lot.lotId).setValueEvent(transactionDto)
    }

    suspend fun setUser(userDto: UserDto):Boolean{
        return usersRef.child(userDto.userData.userId).setValueEvent(userDto)
    }

    suspend fun getPricingVars(root:String):Resource<PricingDataDto>{
        return when(val res=adminRef.child(root).getEvent()){
            is Resource.Error -> Resource.Error<PricingDataDto>(message = res.message)
            is Resource.Loading -> Resource.Loading()
            is Resource.Success -> Resource.Success(data = res.data?.getValue(PricingDataDto::class.java)?:PricingDataDto())
        }
    }

    suspend fun postFrijolLot(lot:LotDto):Resource<String>{
        frijolesTransactions.push().key?.let { pushKey ->
            if (frijolesTransactions.child(pushKey).setValueEvent(TransactionDto(lot=lot.copy(lotId = pushKey)))){
                return Resource.Success(pushKey)
            }else{
                return Resource.Error(message = "failed to insert data")
            }
        }
        return Resource.Error(message = "failed to generate key")
    }

    suspend fun postOffer(userId:String, lotId:String, offerDto: OfferDto):Boolean{
        val ref=frijolesTransactions.child(lotId).child("offers").child(userId)
        return ref.setValueEvent(offerDto)
    }

    suspend fun postReview(userId:String, reviewDto: ReviewDto):Boolean{
        val ref=usersRef.child(userId).child("reviews").child(reviewDto.reviewerId)
        return ref.setValueEvent(reviewDto)
    }

    suspend fun registerUser(userData:UserDataDto):Resource<String>{
        usersRef.push().key?.let { pushKey ->
            if (usersRef.setValueEvent(UserDto(userData = userData.copy(userId = pushKey)))){
                return Resource.Success(pushKey)
            }else{
                return Resource.Error(message = "failed to insert data")
            }
        }
        return Resource.Error(message = "failed to generate key")
    }


    //this turns normal get().addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener) into a suspend function that returns resource containing the Snapshot
    suspend fun DatabaseReference.getEvent(): Resource<DataSnapshot> = suspendCoroutine { continuation ->
        val onSuccessListener =
            OnSuccessListener<DataSnapshot> { snapshot -> continuation.resume(Resource.Success(snapshot)) }
        val onFailureListener = OnFailureListener {continuation.resume(Resource.Error(message = it.message))}
        get().addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener) // Subscribe to the event
    }

    suspend fun DatabaseReference.setValueEvent(value:Any): Boolean = suspendCoroutine { continuation ->
        val onSuccessListener =
            OnSuccessListener<Any>{ continuation.resume(true) }
        val onFailureListener = OnFailureListener {continuation.resume(false)}
        setValue(value).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener) // Subscribe to the event
    }


    //this turns valueEventListener into a suspend function
    @ExperimentalCoroutinesApi
    suspend fun DatabaseReference.valueEventFlow(): Flow<Resource<DataSnapshot>> = callbackFlow {
        val valueEventListener: ValueEventListener =object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySendBlocking(Resource.Success(snapshot))
            }

            override fun onCancelled(error: DatabaseError) {
                trySendBlocking(Resource.Error(message = error.message))
            }
        }
        addValueEventListener(valueEventListener)
        awaitClose{
            removeEventListener(valueEventListener)
        }
    }

}