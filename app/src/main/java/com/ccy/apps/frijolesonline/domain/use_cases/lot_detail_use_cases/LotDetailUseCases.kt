package com.ccy.apps.frijolesonline.domain.use_cases.lot_detail_use_cases

import com.ccy.apps.frijolesonline.domain.use_cases.publish_lot_use_cases.CreateProduceFromMapUseCase

class LotDetailUseCases (
    val getLotDetailUseCase: GetLotDetailUseCase,
    val getPricingDataUseCase: GetPricingDataUseCase,
    val publishOfferUseCase: PublishOfferUseCase,
    val createProduceFromMapUseCase: CreateProduceFromMapUseCase
    )