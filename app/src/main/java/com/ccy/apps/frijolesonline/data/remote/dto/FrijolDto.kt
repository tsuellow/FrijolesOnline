package com.ccy.apps.frijolesonline.data.remote.dto

import com.ccy.apps.frijolesonline.domain.model.Frijol

data class FrijolDto(
    val unitOfMeasurement: String = "qq",
    val variety: String = "Frijol Rojo",
    val humidity: Int = 100,
    val purity: Int = 0,
    val origin:String="Nowhere",
    val harvest: String = "primera",
    val photoPath: String = "",
    val isVerified: Boolean = false,
    val comment: String = "" //agregar curado
):ProduceDto {

    constructor(hashMap: HashMap<String, Any>) : this(
        unitOfMeasurement = hashMap["unitOfMeasurement"] as String,
        variety = hashMap["variety"] as String,
        humidity = (hashMap["humidity"] as Number).toInt(),
        purity = (hashMap["purity"] as Number).toInt(),
        origin = hashMap["origin"] as String,
        harvest = hashMap["harvest"] as String,
        photoPath = hashMap["photoPath"] as String,
        isVerified = hashMap["isVerified"] as Boolean,
        comment = hashMap["comment"] as String
    )

    override fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "unitOfMeasurement" to unitOfMeasurement,
            "variety" to variety,
            "humidity" to humidity,
            "purity" to purity,
            "origin" to origin,
            "harvest" to harvest,
            "photoPath" to photoPath,
            "isVerified" to isVerified,
            "comment" to comment
        )
    }

    override fun toProduce(): Frijol {
        return Frijol(
            unitOfMeasurement = unitOfMeasurement,
            variety = variety,
            humidity = humidity,
            purity = purity,
            origin = origin,
            harvest = harvest,
            photoPath = photoPath,
            isVerified = isVerified,
            comment = comment,
        )
    }

    companion object {


        fun toHashMap(frijolDto: FrijolDto): HashMap<String, Any> {
            return hashMapOf(
                "unitOfMeasurement" to frijolDto.unitOfMeasurement,
                "variety" to frijolDto.variety,
                "humidity" to frijolDto.humidity,
                "purity" to frijolDto.purity,
                "origin" to frijolDto.origin,
                "harvest" to frijolDto.harvest,
                "photoPath" to frijolDto.photoPath,
                "isVerified" to frijolDto.isVerified,
                "comment" to frijolDto.comment
            )
        }
    }
}