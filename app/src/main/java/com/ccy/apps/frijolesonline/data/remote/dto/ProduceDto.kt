package com.ccy.apps.frijolesonline.data.remote.dto

import com.ccy.apps.frijolesonline.domain.model.Produce

interface ProduceDto {
    fun toProduce(): Produce
    fun toHashMap(): HashMap<String, Any>
}