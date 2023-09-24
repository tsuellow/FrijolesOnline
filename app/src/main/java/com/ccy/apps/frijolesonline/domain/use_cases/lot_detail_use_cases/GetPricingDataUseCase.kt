package com.ccy.apps.frijolesonline.domain.use_cases.lot_detail_use_cases

import com.ccy.apps.frijolesonline.common.Resource
import com.ccy.apps.frijolesonline.domain.model.PricingData
import com.ccy.apps.frijolesonline.domain.repository.FrijolesRepository

class GetPricingDataUseCase(private val repository: FrijolesRepository) {

    suspend operator fun  invoke(root:String):PricingData?{
        return when(val res=repository.getPricingData(root)){
            is Resource.Success -> res.data
            else -> null
        }
    }
}