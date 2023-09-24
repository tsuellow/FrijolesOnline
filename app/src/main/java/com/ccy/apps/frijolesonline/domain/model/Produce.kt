package com.ccy.apps.frijolesonline.domain.model

import androidx.compose.runtime.Composable
import com.ccy.apps.frijolesonline.common.PropertyClass
import com.ccy.apps.frijolesonline.data.remote.dto.ProduceDto
import java.util.LinkedHashMap

interface Produce:Orderable,Filterable {

    @Composable
    fun GetEssentials()

    @Composable
    fun PictureNameView()

    @Composable
    fun DisplayProduce()

    fun getProduceName():String

    fun getProperties():HashMap<String,PropertyClass>

    fun getPropertyByName(name:String):Any?

    fun copyByName(name:String, value:Any):Produce

    fun toProduceDto(): ProduceDto

    fun getIsVerified():Boolean

    fun getUnits():String

    fun toLinkedHashMap(): LinkedHashMap<String, Any>




}