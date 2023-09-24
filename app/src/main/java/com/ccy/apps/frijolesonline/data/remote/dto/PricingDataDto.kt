package com.ccy.apps.frijolesonline.data.remote.dto

import com.ccy.apps.frijolesonline.domain.model.PricingData

data class PricingDataDto(val fixed:Int=0,val variable:Int=0){
    fun toPricingData()=PricingData(
        fixed=fixed, variable=variable
    )
}


