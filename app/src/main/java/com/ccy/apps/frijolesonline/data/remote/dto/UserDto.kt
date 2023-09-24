package com.ccy.apps.frijolesonline.data.remote.dto

import androidx.compose.runtime.key
import com.ccy.apps.frijolesonline.domain.model.User

data class UserDto(
    val userData: UserDataDto = UserDataDto(),
    val reviews: Map<String, ReviewDto> = emptyMap(),
    val sales: Map<String, String> = emptyMap(),
    val purchases: Map<String, String> = emptyMap(),
) {
    fun toUser(): User {
        return User(
            userData = userData.toUserData(),
            reviews = HashMap(reviews.mapValues { it.value.toReview() }),
            sales = sales,
            purchases = purchases,
        )
    }
}
