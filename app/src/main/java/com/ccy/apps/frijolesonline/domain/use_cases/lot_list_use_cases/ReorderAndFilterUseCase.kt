package com.ccy.apps.frijolesonline.domain.use_cases.lot_list_use_cases

import com.ccy.apps.frijolesonline.common.OrderType
import com.ccy.apps.frijolesonline.common.PropertyType
import com.ccy.apps.frijolesonline.domain.model.Lot

class ReorderAndFilterUseCase {

    operator fun invoke(
        rawList:List<Lot> = emptyList<Lot>(),
        filterArray: HashMap<String, PropertyType> = HashMap(),
        order: String = "dateUpdated",
        orderType: OrderType = OrderType.Ascending
    ):List<Lot>{
        //filter
        val resFiltered = rawList.filter { it.passesFilter(filterArray) }
        //reorder data
        when (orderType) {
            is OrderType.Ascending -> {
                when {
                    filterArray[order] is PropertyType.IntRange -> {
                        return resFiltered.sortedBy {
                            (it.getOrderingCriterion(
                                order
                            ) as Int)
                        }
                    }
                    filterArray[order] is PropertyType.Dichotomous -> {
                        return resFiltered.sortedBy {
                            (it.getOrderingCriterion(
                                order
                            ) as Boolean)
                        }
                    }
                    else -> {
                        return resFiltered.sortedBy {
                            (it.getOrderingCriterion(
                                order
                            ) as String)
                        }
                    }
                }
            }
            OrderType.Descending -> {
                when {
                    filterArray[order] is PropertyType.IntRange -> {
                        return resFiltered.sortedByDescending {
                            (it.getOrderingCriterion(
                                order
                            ) as Int)
                        }
                    }
                    filterArray[order] is PropertyType.Dichotomous -> {
                        return resFiltered.sortedByDescending {
                            (it.getOrderingCriterion(
                                order
                            ) as Boolean)
                        }
                    }
                    else -> {
                        return resFiltered.sortedByDescending {
                            (it.getOrderingCriterion(
                                order
                            ) as String)
                        }
                    }
                }
            }
        }
    }
}