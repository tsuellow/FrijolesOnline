package com.ccy.apps.frijolesonline.domain.use_cases.lot_list_use_cases

import com.ccy.apps.frijolesonline.common.PropertyClass
import com.ccy.apps.frijolesonline.domain.model.Frijol
import com.ccy.apps.frijolesonline.domain.model.Lot

class GetOrderingsUseCase {

    operator fun invoke(produce:String):HashMap<String,PropertyClass>{
        val lotOrderings= Lot.Features.getOrderableFeatures()
        val produceOrderings=when{
            produce.contentEquals("frijol") -> {
                Frijol.Features.getOrderableFeatures()
            }
            else -> {
                emptyMap<String,PropertyClass>()
            }
        }
        lotOrderings.putAll(produceOrderings)
        return lotOrderings
    }
}