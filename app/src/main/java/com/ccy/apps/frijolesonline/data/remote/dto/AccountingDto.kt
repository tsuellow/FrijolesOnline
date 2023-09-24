package com.ccy.apps.frijolesonline.data.remote.dto

import com.ccy.apps.frijolesonline.domain.model.Accounting

data class AccountingDto(
    val dateTime: String = "never",
    val lotId: String = "",
    val calculatedIncomeNio: Int = 0,
    val originator: String = "",
    val concept:String=""
){
    fun toAccounting():Accounting{
        return Accounting(
            dateTime = dateTime,
            lotId = lotId,
            calculatedIncomeNio = calculatedIncomeNio,
            originator = originator,
            concept = concept,
        )
    }
}
