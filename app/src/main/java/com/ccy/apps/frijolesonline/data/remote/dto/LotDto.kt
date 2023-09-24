package com.ccy.apps.frijolesonline.data.remote.dto

import com.ccy.apps.frijolesonline.domain.model.Frijol
import com.ccy.apps.frijolesonline.domain.model.Lot


data class LotDto(
    val lotId: String = "0",
    val quantity: Int = 0,
    val unitPriceOrigin: Int = 0,
    val unitPriceDestination: Int = 0,
    val destination: String? = null,
    val minSale:Int=0,
    val observations:String="",
    val seller: UserDataDto = UserDataDto(),
    val dateCreated: String = "never",
    val dateUpdated: String = "never",
    val produceCategory:String="Frijol",
    val produce:HashMap<String,Any> = HashMap()
) {


    fun toLot(): Lot {
        return Lot(
            lotId = lotId,
            quantity = quantity,
            unitPriceOrigin = unitPriceOrigin,
            unitPriceDestination = unitPriceDestination,
            destination = destination,
            minSale = minSale,
            observations=observations,
            seller = seller.toUserData(),
            dateCreated = dateCreated,
            dateUpdated = dateUpdated,
            produceCategory = produceCategory,
            produce = toProduceDto().toProduce().toLinkedHashMap()
        )
    }

    fun toProduceDto():ProduceDto{
        return when{
            produceCategory.contentEquals(Frijol.Features.categoryName) ->{
                FrijolDto(produce)
            }
            else -> {
                FrijolDto(produce)
            }
        }
    }
}
