package com.ccy.apps.frijolesonline.domain.model

import com.ccy.apps.frijolesonline.data.remote.dto.ReviewDto

data class Review(
    val reviewerId: String = "",
    val reviewerName: String = "",
    val lotId:String="",
    val comment: String = "",
    val stars: Int = 0
){
    fun toReviewDto(): ReviewDto {
        return ReviewDto(
            reviewerId = reviewerId,
            reviewerName = reviewerName,
            lotId = lotId,
            comment = comment,
            stars = stars,
        )
    }
}
