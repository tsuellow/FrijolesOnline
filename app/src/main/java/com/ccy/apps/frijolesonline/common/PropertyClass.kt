package com.ccy.apps.frijolesonline.common

import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.reflect.KClass

data class PropertyClass(
    val name: String,
    val description: String,
    val helpText: String? = null,
    val propertyType: PropertyType,
    val icon: ImageVector? = null,
    val isRequired:Boolean=true
){
    fun hasError(input:String):String{
        if (input.isEmpty() && isRequired){
            return "cannot be empty"
        }else{
            if (input.isEmpty() && !isRequired){
                return ""
            }else{
                return when(propertyType){
                    is PropertyType.IntRange -> {
                        if(!propertyType.inRange(input.filter { it.isDigit() }.toIntOrNull())){
                            if (propertyType.maxRange == Int.MAX_VALUE) {
                                "has to be greater than ${propertyType.minRange}"
                            }else {
                                "not in range ${propertyType.minRange} to ${propertyType.maxRange}"
                            }
                        } else {
                            ""
                        }
                    }
                    is PropertyType.StringCategory -> {
                        if (input !in propertyType.listRange)  "invalid option" else ""
                    }
                    else-> {
                        ""
                    }
                }
            }
        }
    }
}

sealed class PropertyType() {
    data class IntRange(val min: Int? = null, val max: Int? = null, val minRange:Int?=null, val maxRange:Int?=null, val unit:String="") :
        PropertyType(){
            fun passesFilter(value:Int):Boolean{
                val aboveMin= min?.let { value>=it } ?:true
                val belowMax= max?.let { value<=it } ?:true
                return aboveMin && belowMax
            }
            fun inRange(value:Int?):Boolean{
                val aboveMin= value?.let {  minRange?.let { value>=it } ?:true}?:true
                val belowMax= value?.let {  maxRange?.let { value<=it } ?:true}?:true
                return aboveMin && belowMax
            }
        }

    data class StringCategory(
        val list: List<String> = emptyList<String>(),
        val listRange:List<String> = emptyList()
    ) : PropertyType(){
        fun passesFilter(value:String):Boolean{
            return list.isEmpty() || list.contains(value)
        }
    }

    data class DateTime(
        val from: String? = null,
        val to: String? = null
    ) : PropertyType(){
        fun passesFilter(value:String):Boolean{
            val aboveMin= from?.let { value>=it } ?:true
            val belowMax= to?.let { value<=it } ?:true
            return aboveMin && belowMax
        }
    }

    data class Dichotomous(val yes: Boolean = true, val no: Boolean = true) : PropertyType(){
        fun passesFilter(value:Boolean):Boolean{
            return ((value && yes) || (!value && no))
        }
    }
    data class FreeText(val text: String? = "") : PropertyType(){
        fun passesFilter(value:String):Boolean{
            return text?.let { value.contains(it) }?:true
        }
    }

}

fun PropertyType.isActive(): Boolean {
    return when(this){
        is PropertyType.IntRange -> {(min != null) || (max != null)}
        is PropertyType.DateTime -> {(from != null) || (to != null)}
        is PropertyType.Dichotomous -> !yes||!no
        is PropertyType.FreeText -> text!=null
        is PropertyType.StringCategory -> list.isNotEmpty()
    }
}

fun PropertyType.removeFilter():PropertyType {
    return when(this){
        is PropertyType.IntRange -> this.copy(min=null,max=null)
        is PropertyType.DateTime -> this.copy(from=null,to=null)
        is PropertyType.Dichotomous -> this.copy(yes=true,no=true)
        is PropertyType.FreeText -> this.copy(text=null)
        is PropertyType.StringCategory -> this.copy(list= emptyList())
    }
}
