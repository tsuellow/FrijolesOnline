package com.ccy.apps.frijolesonline.common

sealed class LotOrder(open var orderType:OrderType){
    data class ByDate(override var orderType:OrderType = OrderType.Descending):LotOrder(orderType)
    data class ByHumidity(override var orderType:OrderType= OrderType.Ascending):LotOrder(orderType)
    data class ByPurity(override var orderType:OrderType= OrderType.Descending):LotOrder(orderType)
    data class ByQuantity(override var orderType:OrderType = OrderType.Descending):LotOrder(orderType)
    data class ByPriceOrigin(override var orderType:OrderType= OrderType.Ascending):LotOrder(orderType)
    data class ByPriceDestination(override var orderType:OrderType= OrderType.Ascending):LotOrder(orderType)
}
