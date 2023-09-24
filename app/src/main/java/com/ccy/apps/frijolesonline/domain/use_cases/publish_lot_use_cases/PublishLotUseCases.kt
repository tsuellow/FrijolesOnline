package com.ccy.apps.frijolesonline.domain.use_cases.publish_lot_use_cases

data class PublishLotUseCases (
    val getProducePropertyMapUseCase: GetProducePropertyMapUseCase,
    val createProduceFromMapUseCase: CreateProduceFromMapUseCase,
    val submitLotUseCase: SubmitLotUseCase
    )