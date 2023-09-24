package com.ccy.apps.frijolesonline.domain.model

import com.ccy.apps.frijolesonline.common.PropertyClass

interface Editable {
    fun getEditables():LinkedHashMap<String,PropertyClass>

    fun createFromMap(hashMap: HashMap<String, Any?>):Produce
}