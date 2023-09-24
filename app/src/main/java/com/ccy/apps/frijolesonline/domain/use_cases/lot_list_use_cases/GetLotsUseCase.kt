package com.ccy.apps.frijolesonline.domain.use_cases.lot_list_use_cases

import android.util.Log
import com.ccy.apps.frijolesonline.common.*
import com.ccy.apps.frijolesonline.domain.model.Frijol
import com.ccy.apps.frijolesonline.domain.model.Lot
import com.ccy.apps.frijolesonline.domain.model.Produce
import com.ccy.apps.frijolesonline.domain.repository.FrijolesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLotsUseCase(
    private val repository: FrijolesRepository
) {

    operator fun invoke(
        produce:String="frijoles",
        filterArray: HashMap<String, PropertyType> = HashMap(),
        order: String = "dateUpdated",
        orderType: OrderType = OrderType.Ascending
    ): Flow<Resource<List<Lot>>> = flow {
        try{
            emit(Resource.Loading<List<Lot>>())

            val res = repository.getCurrentLotList()
            when (res) {
                is Resource.Success -> {
                    emit(res)
                } else -> {
                    emit(res)
                }
            }
        }catch (e:Exception){
            Log.d("testiando3",e.toString())
            emit(Resource.Error(message = e.message?:"unknown error"))
        }

    }
}