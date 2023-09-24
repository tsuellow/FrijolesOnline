package com.ccy.apps.frijolesonline.domain.model

import com.ccy.apps.frijolesonline.data.remote.dto.UserDataDto

data class UserData(
    val userId:String="",
    val firstName: String="",
    val lastName: String="",
    val municipality: String="",
    val phone: String="",
    val photoPath: String="",
){
    fun toUserDataDto(): UserDataDto {
        return UserDataDto(
            userId = userId,
            firstName = firstName,
            lastName = lastName,
            municipality = municipality,
            phone = phone,
            photoPath = photoPath,
        )
    }
}
