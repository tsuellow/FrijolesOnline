package com.ccy.apps.frijolesonline.common

sealed class OrderType{
    object Ascending:OrderType()
    object Descending:OrderType()
}
