package com.ccy.apps.frijolesonline.domain.use_cases.lot_list_use_cases

data class LotListUseCases(
    val getLotsUseCase: GetLotsUseCase,
    val getOrderingsUseCase: GetOrderingsUseCase,
    val getFilterArrayUseCase: GetFilterArrayUseCase,
    val reorderAndFilterUseCase: ReorderAndFilterUseCase
)