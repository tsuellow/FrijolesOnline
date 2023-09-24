package com.ccy.apps.frijolesonline.common

sealed class FilterRange(){
    class HumidityRange(val low:Int?=null, val high:Int?=null):FilterRange()
    class PurityRange(val low:Int?=null, val high:Int?=null):FilterRange()
    class PriceOriginRange(val low:Int?=null, val high:Int?=null):FilterRange()
    class PriceDestinationRange(val low:Int?=null, val high:Int?=null):FilterRange()
    class QuantityRange(val low:Int?=null, val high:Int?=null):FilterRange()
    class LocationRange(val places:Array<String> = emptyArray()):FilterRange()
}

data class FilterArray(
    val humidityRange: FilterRange.HumidityRange = FilterRange.HumidityRange(),
    val purityRange: FilterRange.PurityRange = FilterRange.PurityRange(),
    val priceOriginRange: FilterRange.PriceOriginRange = FilterRange.PriceOriginRange(),
    val priceDestinationRange: FilterRange.PriceDestinationRange = FilterRange.PriceDestinationRange(),
    val quantityRange: FilterRange.QuantityRange = FilterRange.QuantityRange(),
    val locationRange: FilterRange.LocationRange = FilterRange.LocationRange(),
)
