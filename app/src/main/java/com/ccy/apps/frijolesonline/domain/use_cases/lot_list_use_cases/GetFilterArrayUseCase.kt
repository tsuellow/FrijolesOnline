package com.ccy.apps.frijolesonline.domain.use_cases.lot_list_use_cases

import com.ccy.apps.frijolesonline.common.PropertyClass
import com.ccy.apps.frijolesonline.domain.model.Frijol
import com.ccy.apps.frijolesonline.domain.model.Lot

class GetFilterArrayUseCase {

    operator fun invoke(produce:String):LinkedHashMap<String,PropertyClass> {
        val lotFilters= Lot.getFilterArray()
        val produceFilters=when{
            produce.contentEquals("frijol") -> {
                Frijol.Features.getFilterArray()
            }
            else -> {
                emptyMap<String,PropertyClass>()
            }
        }
        lotFilters.putAll(produceFilters)
        return lotFilters

    }
}