package com.ccy.apps.frijolesonline.data.remote.dto

import com.ccy.apps.frijolesonline.domain.model.Review

data class ReviewDto(
    val reviewerId: String = "",
    val reviewerName: String = "",
    val lotId: String = "",
    val comment: String = "",
    val stars: Int = 0
) {
    fun toReview(): Review {
        return Review(
            reviewerId = reviewerId,
            reviewerName = reviewerName,
            lotId = lotId,
            comment = comment,
            stars = stars,
        )
    }
}
