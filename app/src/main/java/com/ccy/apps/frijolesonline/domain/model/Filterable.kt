package com.ccy.apps.frijolesonline.domain.model

import com.ccy.apps.frijolesonline.common.PropertyClass
import com.ccy.apps.frijolesonline.common.PropertyType

interface Filterable {
    fun passesFilter(filters:HashMap<String, PropertyType>):Boolean
}

interface FilterableFeatures {
    fun getFilterArray():LinkedHashMap<String, PropertyClass>
}