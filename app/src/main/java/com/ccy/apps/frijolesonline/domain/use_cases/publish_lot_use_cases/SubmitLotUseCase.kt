package com.ccy.apps.frijolesonline.domain.use_cases.publish_lot_use_cases

import com.ccy.apps.frijolesonline.common.Resource
import com.ccy.apps.frijolesonline.domain.model.Lot
import com.ccy.apps.frijolesonline.domain.repository.FrijolesRepository

class SubmitLotUseCase(
    private val repository: FrijolesRepository
) {
    suspend operator fun invoke(lot: Lot):Resource<String>{
        return repository.postFrijolLot(lot)
    }
}