package com.ccy.apps.frijolesonline.util

sealed class Screen(val route:String) {
    object EntryScreen:Screen(route = "entryScreen")

    object LotList:Screen(route = "lotList")

    object PublishLot:Screen(route = "publishLot")

    object LotDetail:Screen(route = "lotDetail"){
        fun withId( root:String,  id:String):String{
            return "$route/$root/$id"
        }
    }


}