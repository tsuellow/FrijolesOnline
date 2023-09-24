package com.ccy.apps.frijolesonline.domain.model

import com.ccy.apps.frijolesonline.common.PropertyClass

interface Orderable {
    fun getOrderingCriterion(property:String):Any
}

interface OrderableFeatures {
    fun getOrderableFeatures():HashMap<String,PropertyClass>
}