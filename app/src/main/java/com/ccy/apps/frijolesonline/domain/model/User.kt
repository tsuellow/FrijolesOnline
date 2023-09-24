package com.ccy.apps.frijolesonline.domain.model

import com.ccy.apps.frijolesonline.data.remote.dto.UserDto

data class User(
    val userData: UserData = UserData(),
    val reviews:Map<String, Review> = emptyMap(),
    val sales:Map<String,String> = emptyMap(),
    val purchases:Map<String,String> = emptyMap(),
){
    fun toUserDto(): UserDto {
        return UserDto(
            userData = userData.toUserDataDto(),
            reviews = HashMap(reviews.mapValues { it.value.toReviewDto() }),
            sales = sales,
            purchases = purchases,
        )
    }
}
