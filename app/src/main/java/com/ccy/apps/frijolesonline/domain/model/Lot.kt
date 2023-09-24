package com.ccy.apps.frijolesonline.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.ProductionQuantityLimits
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material.icons.outlined.RequestQuote
import com.ccy.apps.frijolesonline.common.PropertyClass
import com.ccy.apps.frijolesonline.common.PropertyType
import com.ccy.apps.frijolesonline.data.remote.dto.LotDto
import com.ccy.apps.frijolesonline.presentation.common_components.toDateTimeString
import java.util.Date


data class Lot(
    val lotId: String = "0",
    val quantity: Int = 0,
    val unitPriceOrigin: Int = 0,
    val unitPriceDestination: Int = 0,
    val destination: String? = null,
    val minSale: Int = 0,
    val observations:String="",
    val seller: UserData = UserData(),
    val dateCreated: String = Date().toDateTimeString(),
    val dateUpdated: String = Date().toDateTimeString(),
    val produceCategory: String = Frijol.categoryName,
    val produce: LinkedHashMap<String, Any> = Frijol().toLinkedHashMap()
) : Filterable, Orderable {

    fun toLotDto(): LotDto {
        return LotDto(
            lotId = lotId,
            quantity = quantity,
            unitPriceOrigin = unitPriceOrigin,
            unitPriceDestination = unitPriceDestination,
            destination = destination,
            minSale = minSale,
            observations=observations,
            seller = seller.toUserDataDto(),
            dateCreated = dateCreated,
            dateUpdated = dateUpdated,
            produceCategory = produceCategory,
            produce = toProduce().toProduceDto().toHashMap()
        )
    }

    fun toProduce(): Produce {
        return when {
            produceCategory.contentEquals(Frijol.Features.categoryName) -> {
                Frijol(produce)
            }

            else -> {
                Frijol(produce)
            }
        }
    }

    companion object Features : FilterableFeatures, OrderableFeatures {

        sealed class Property(val key: String) {
            object Quantity : Property("quantity")
            object UnitPriceOrigin : Property("priceOrigin")
            object UnitPriceDestination : Property("priceDestination")
            object DateUpdated : Property("dateUpdated")
            object Destination : Property("destination")
            object MinimumSale : Property("minSale")
            object Observations : Property("observations")
        }

        val propertyMap = linkedMapOf<String, PropertyClass>(

            Property.Quantity.key to PropertyClass(
                name = "Quantity",
                propertyType = PropertyType.IntRange(
                    minRange = 0,
                    maxRange = Int.MAX_VALUE,
                    unit = "units"
                ),
                description = "Quantity on sale",
                icon = Icons.Filled.LocalShipping
            ),
            Property.UnitPriceOrigin.key to PropertyClass(
                name = "Unit price - Origin",
                propertyType = PropertyType.IntRange(
                    minRange = 0,
                    maxRange = Int.MAX_VALUE,
                    unit = "C$/unit"
                ),
                description = "Price on production site",
                icon = Icons.Filled.RequestQuote
            ),
            Property.UnitPriceDestination.key to PropertyClass(
                name = "Unit price - Destination",
                propertyType = PropertyType.IntRange(
                    minRange = 0,
                    maxRange = Int.MAX_VALUE,
                    unit = "C$/unit"
                ),
                description = "Price including transport",
                icon = Icons.Outlined.RequestQuote,
                isRequired = false
            ),
            Property.DateUpdated.key to PropertyClass(
                name = "dateUpdated",
                propertyType = PropertyType.DateTime(from = null, to = null),
                description = "Date of publication",
                icon = Icons.Filled.Today
            ),
            Property.Destination.key to PropertyClass(
                name = "Destination",
                propertyType = PropertyType.StringCategory(listRange = listOf("ESTELI", "SEBACO")),
                description = "Place of transaction",
                icon = Icons.Filled.TravelExplore,
                isRequired = false
            ),
            Property.MinimumSale.key to PropertyClass(
                name = "Minimum order - Quantity",
                propertyType = PropertyType.IntRange(
                    minRange = 0,
                    maxRange = Int.MAX_VALUE,
                    unit = "units"
                ),
                description = "Minimum quantity to order",
                icon = Icons.Filled.ProductionQuantityLimits,
                isRequired = false
            ),
            Property.Observations.key to PropertyClass(
                name = "Observations",
                propertyType = PropertyType.FreeText(),
                description = "Observations: Lot delivery details",
                icon = Icons.Filled.Chat,
                isRequired = false
            ),
        )

        fun getLotProperty(key: String): PropertyClass {
            return propertyMap[key] ?:
            PropertyClass(
                name = "Comment",
                description = "free text",
                propertyType = PropertyType.FreeText()
            )
        }

        override fun getFilterArray(): LinkedHashMap<String, PropertyClass> {
            return propertyMap
        }

        override fun getOrderableFeatures(): HashMap<String, PropertyClass> {
            return HashMap(propertyMap.filterKeys {
                it in listOf(
                    Property.Quantity.key,
                    Property.UnitPriceOrigin.key,
                    Property.DateUpdated.key
                )
            })
        }

        fun getEditables(units: String): LinkedHashMap<String, PropertyClass> {
            val editables = listOf(
                Property.Quantity.key,
                Property.UnitPriceOrigin.key,
                Property.MinimumSale.key,
                Property.Destination.key,
                Property.UnitPriceDestination.key,
                Property.Observations.key,

            )
            val editableMap = LinkedHashMap<String, PropertyClass>()
            propertyMapWithUnits(units).forEach { (k, v) ->
                if (k in editables) {
                    editableMap[k] = v
                }
            }
            return editableMap
        }

        fun propertyMapWithUnits(units: String): LinkedHashMap<String, PropertyClass> {
            val map: LinkedHashMap<String, PropertyClass> = LinkedHashMap()
            map.putAll(propertyMap)
            propertyMap[Property.Quantity.key]?.let {
                map[Property.Quantity.key] =
                    it.copy(propertyType = (it.propertyType as PropertyType.IntRange).copy(unit = units))
            }
            propertyMap[Property.UnitPriceOrigin.key]?.let {
                map[Property.UnitPriceOrigin.key] =
                    it.copy(propertyType = (it.propertyType as PropertyType.IntRange).copy(unit = "C$/$units"))
            }
            propertyMap[Property.UnitPriceDestination.key]?.let {
                map[Property.UnitPriceDestination.key] =
                    it.copy(propertyType = (it.propertyType as PropertyType.IntRange).copy(unit = "C$/$units"))
            }
            propertyMap[Property.MinimumSale.key]?.let {
                map[Property.MinimumSale.key] =
                    it.copy(propertyType = (it.propertyType as PropertyType.IntRange).copy(unit = units))
            }
            return map

        }

        fun createFromMap(hashMap: HashMap<String, Any?>, produce: Produce): Lot {
            var lot = Lot()
            hashMap.forEach { property ->
                property.value?.let {
                    lot = when (property.key) {
                        Property.Quantity.key -> lot.copy(quantity = property.value as Int)
                        Property.UnitPriceOrigin.key -> lot.copy(unitPriceOrigin = property.value as Int)
                        Property.UnitPriceDestination.key -> lot.copy(unitPriceDestination = property.value as Int)
                        Property.Destination.key -> lot.copy(destination = property.value as String)
                        Property.MinimumSale.key -> lot.copy(minSale = property.value as Int)
                        Property.Observations.key -> lot.copy(observations = property.value as String)
                        else -> lot.copy()
                    }
                }
            }
            lot = lot.copy(produce = produce.toLinkedHashMap())
            return lot
        }
    }


    override fun passesFilter(filters: HashMap<String, PropertyType>): Boolean {
        val quantityPass =
            (filters[Property.Quantity.key] as PropertyType.IntRange).passesFilter(quantity)
        val priceDestinationPass =
            (filters[Property.UnitPriceDestination.key] as PropertyType.IntRange).passesFilter(
                unitPriceDestination
            )
        val priceOriginPass =
            (filters[Property.UnitPriceOrigin.key] as PropertyType.IntRange).passesFilter(
                unitPriceOrigin
            )
        val dateUpdatedPass =
            (filters[Property.DateUpdated.key] as PropertyType.DateTime).passesFilter(dateUpdated)
        return quantityPass && priceDestinationPass && priceOriginPass && dateUpdatedPass && toProduce().passesFilter(
            filters
        )
    }

    override fun getOrderingCriterion(property: String): Any {
        return when {
            property.contentEquals(Property.Quantity.key) -> quantity
            property.contentEquals(Property.UnitPriceOrigin.key) -> unitPriceOrigin
            property.contentEquals(Property.UnitPriceDestination.key) -> unitPriceDestination
            property.contentEquals(Property.DateUpdated.key) -> dateUpdated
            else -> {
                toProduce().getOrderingCriterion(property)
            }
        }
    }
}
