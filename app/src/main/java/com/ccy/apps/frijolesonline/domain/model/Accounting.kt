package com.ccy.apps.frijolesonline.domain.model

import com.ccy.apps.frijolesonline.data.remote.dto.AccountingDto

data class Accounting(
    val dateTime: String = "never",
    val lotId: String = "",
    val calculatedIncomeNio: Int = 0,
    val originator: String = "",
    val concept:String=""
){
    fun toAccountingDto():AccountingDto{
        return AccountingDto(
            dateTime = dateTime,
            lotId = lotId,
            calculatedIncomeNio = calculatedIncomeNio,
            originator = originator,
            concept = concept,
        )
    }
}
