package com.ccy.apps.frijolesonline.domain.use_cases.lot_detail_use_cases

import com.ccy.apps.frijolesonline.common.Resource
import com.ccy.apps.frijolesonline.domain.model.Lot
import com.ccy.apps.frijolesonline.domain.repository.FrijolesRepository
import kotlinx.coroutines.flow.Flow

class GetLotDetailUseCase(val repository: FrijolesRepository){
    operator fun invoke(root:String, pushId:String): Flow<Resource<Lot>> {
        return repository.getLot(root,pushId)
    }
}