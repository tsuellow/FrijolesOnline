package com.ccy.apps.frijolesonline.data.remote.dto

import com.ccy.apps.frijolesonline.domain.model.UserData

data class UserDataDto(
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val municipality: String = "",
    val phone: String = "",
    val photoPath: String = "",
) {
    fun toUserData(): UserData {
        return UserData(
            userId = userId,
            firstName = firstName,
            lastName = lastName,
            municipality = municipality,
            phone = phone,
            photoPath = photoPath,
        )
    }
}
