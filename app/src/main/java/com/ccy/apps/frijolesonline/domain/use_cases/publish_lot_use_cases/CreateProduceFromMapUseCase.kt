package com.ccy.apps.frijolesonline.domain.use_cases.publish_lot_use_cases

import com.ccy.apps.frijolesonline.domain.model.Frijol
import com.ccy.apps.frijolesonline.domain.model.Produce

class CreateProduceFromMapUseCase {
    operator fun invoke(produceType:String, map: HashMap<String, Any?>): Produce {
        return when(produceType){
            Frijol.categoryName->{
                Frijol.Features.createFromMap(map)
            }

            else -> {Frijol.Features.createFromMap(map)}
        }
    }
}