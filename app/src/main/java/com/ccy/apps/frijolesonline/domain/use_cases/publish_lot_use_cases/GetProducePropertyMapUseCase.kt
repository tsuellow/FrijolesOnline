package com.ccy.apps.frijolesonline.domain.use_cases.publish_lot_use_cases

import com.ccy.apps.frijolesonline.common.PropertyClass
import com.ccy.apps.frijolesonline.domain.model.Frijol

class GetProducePropertyMapUseCase {

    operator fun invoke(produce:String):LinkedHashMap<String,PropertyClass>{
        return Frijol.Features.getEditables()

    }
}