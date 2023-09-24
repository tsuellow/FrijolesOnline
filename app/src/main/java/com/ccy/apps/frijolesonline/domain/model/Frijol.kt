package com.ccy.apps.frijolesonline.domain.model

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ccy.apps.frijolesonline.R
import com.ccy.apps.frijolesonline.common.Constants
import com.ccy.apps.frijolesonline.common.PropertyClass
import com.ccy.apps.frijolesonline.common.PropertyType
import com.ccy.apps.frijolesonline.data.remote.dto.FrijolDto
import com.ccy.apps.frijolesonline.data.remote.dto.ProduceDto
import com.ccy.apps.frijolesonline.presentation.common_components.getMunicipality
import com.ccy.apps.frijolesonline.presentation.publish_lot.components.DisplayProperty
import kotlin.collections.LinkedHashMap

data class Frijol(
    val unitOfMeasurement: String = "qq",
    val variety: String = "Frijol Rojo",
    val humidity: Int = 100,
    val purity: Int = 0,
    val origin: String = "Nowhere",
    val harvest: String = "primera",
    val photoPath: String = "",
    val isVerified: Boolean = false,
    val comment: String = ""
) : Produce {

    constructor(hashMap: HashMap<String, Any>) : this(
        unitOfMeasurement = hashMap["unitOfMeasurement"] as String,
        variety = hashMap["variety"] as String,
        humidity = hashMap["humidity"] as Int,
        purity = hashMap["purity"] as Int,
        origin = hashMap["origin"] as String,
        harvest = hashMap["harvest"] as String,
        photoPath = hashMap["photoPath"] as String,
        isVerified = hashMap["isVerified"] as Boolean,
        comment = hashMap["comment"] as String
    )


    companion object Features : OrderableFeatures, FilterableFeatures, Editable {

        const val categoryName = "frijol"

        sealed class Property(val key: String) {
            object Variety : Property("variety")
            object Humidity : Property("humidity")
            object Purity : Property("purity")
            object Origin : Property("origin")
            object Harvest : Property("harvest")
            object IsVerified : Property("isVerified")

            //others
            object Comment : Property("comment")
            object PhotoPath : Property("photoPath")
            object UnitOfMeasurement : Property("unitOfMeasurement")
        }

        val propertyMap: LinkedHashMap<String, PropertyClass> = linkedMapOf(
            Property.Variety.key to PropertyClass(
                name = "variety",
                propertyType = PropertyType.StringCategory(
                    listRange = listOf("Frijol Rojo", "Frijol Negro", "Frijol Rojo Claro")
                ),
                description = "Product Variety",
                icon = Icons.Filled.Checklist
            ),
            Property.UnitOfMeasurement.key to PropertyClass(
                name = "unit",
                propertyType = PropertyType.StringCategory(
                    listRange = listOf("qq (quintales 100lbs)")
                ),
                description = "unit of measurement",
            ),
            Property.Humidity.key to PropertyClass(
                name = "humidity",
                propertyType = PropertyType.IntRange(minRange = 0, maxRange = 100, unit = "%"),
                description = "Degree of Humidity",
                helpText = "suave al diente = 20  chicloso al diente 15  duro al diente 10",
                icon = Icons.Filled.WaterDrop
            ),
            Property.Purity.key to PropertyClass(
                name = "purity",
                propertyType = PropertyType.IntRange(minRange = 0, maxRange = 100, unit = "%"),
                description = "Degree of Purity",
                helpText = "pese 200 gramos de frijoles. limpie todas la impurezas y vuelva a pesar el frijol limpio. Divida el nuevo peso entre 200",
                icon = Icons.Filled.SavedSearch
            ),
            Property.Origin.key to PropertyClass(
                name = "origin",
                propertyType = PropertyType.StringCategory(listRange = Constants.MUNICIPIOS),
                description = "Product Origin, Municipality",
                helpText = "Municipio de origen del Frijol",
                icon = Icons.Filled.Place
            ),
            Property.Harvest.key to PropertyClass(
                name = "harvest",
                propertyType = PropertyType.StringCategory(
                    listRange = listOf(
                        "primera",
                        "postrera",
                        "apante",
                        "otra"
                    )
                ),
                description = "Season of Harvest",
                helpText = "La temporada en la q se cosecho el frijol",
                icon = Icons.Filled.Agriculture
            ),
            Property.IsVerified.key to PropertyClass(
                name = "is verified",
                propertyType = PropertyType.Dichotomous(),
                description = "Verification by FrijolesOnline",
                helpText = "All relevant data like humidity and purity was corroborated scientifically by our Team",
                icon = Icons.Filled.VerifiedUser
            ),
            Property.Comment.key to PropertyClass(
                name = "Comment",
                propertyType = PropertyType.FreeText(),
                description = "Comment",
                icon = Icons.Filled.Comment
            ),
            Property.PhotoPath.key to PropertyClass(
                name = "Photo",
                propertyType = PropertyType.FreeText(),
                description = "Photo",
                icon = Icons.Filled.Comment
            ),
        )


        override fun getOrderableFeatures(): HashMap<String, PropertyClass> {
            return HashMap(propertyMap.filterKeys {
                it in listOf(
                    Property.Humidity.key,
                    Property.Purity.key,
                    Property.Harvest.key,
                    Property.Origin.key
                )
            })
        }

        override fun getFilterArray(): LinkedHashMap<String, PropertyClass> {
            val filterables = listOf(
                Property.Humidity.key,
                Property.Purity.key,
                Property.Origin.key,
                Property.Harvest.key,
                Property.Variety.key,
                Property.IsVerified.key,
            )
            val filterMap = LinkedHashMap<String, PropertyClass>()
            propertyMap.forEach { (k, v) ->
                if (k in filterables) {
                    filterMap[k] = v
                }
            }
            return filterMap
        }

        @Composable
        fun CreateInstance(onCompleted: (Frijol) -> Unit) {

        }

        override fun getEditables(): LinkedHashMap<String, PropertyClass> {
            val editables = listOf(
                Property.Variety.key,
                Property.Humidity.key,
                Property.Purity.key,
                Property.Origin.key,
                Property.Harvest.key,
            )
            val editableMap = LinkedHashMap<String, PropertyClass>()
            propertyMap.forEach { (k, v) ->
                if (k in editables) {
                    editableMap[k] = v
                }
            }
            return editableMap
        }

        override fun createFromMap(hashMap: HashMap<String, Any?>): Produce {
            var frijol = Frijol()
            hashMap.forEach { property ->
                property.value?.let {
                    frijol = when (property.key) {
                        Property.UnitOfMeasurement.key -> frijol.copy(unitOfMeasurement = property.value as String)
                        Property.Variety.key -> frijol.copy(variety = property.value as String)
                        Property.Humidity.key -> frijol.copy(humidity = property.value as Int)
                        Property.Purity.key -> frijol.copy(purity = property.value as Int)
                        Property.Origin.key -> frijol.copy(origin = property.value as String)
                        Property.Harvest.key -> frijol.copy(harvest = property.value as String)
                        Property.PhotoPath.key -> frijol.copy(photoPath = property.value as String)
                        Property.IsVerified.key -> frijol.copy(isVerified = property.value as Boolean)
                        Property.Comment.key -> frijol.copy(comment = property.value as String)
                        else -> frijol.copy()
                    }
                }
            }
            return frijol
        }

        fun getProperty(key: String): PropertyClass {
            return propertyMap[key] ?: PropertyClass(
                name = "Comment",
                description = "free text",
                propertyType = PropertyType.FreeText()
            )
        }
    }

    fun getSummary(): String {
        return "summary"
    }

    @Composable
    override fun GetEssentials() {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                imageVector = Icons.Filled.WaterDrop,
                contentDescription = "Humidity",
                modifier = Modifier.size(18.dp)
            )

            Text(text = "${humidity}%")

            Spacer(modifier = Modifier.padding(horizontal = 3.dp))

            Icon(
                imageVector = Icons.Filled.SavedSearch,
                contentDescription = "Purity",
                modifier = Modifier.size(18.dp)
            )

            Text(text = "${purity}%")

            Spacer(modifier = Modifier.padding(horizontal = 3.dp))

            Icon(
                imageVector = Icons.Filled.Place,
                contentDescription = "Place",
                modifier = Modifier.size(18.dp)
            )

            Text(text = origin.getMunicipality(), overflow = TextOverflow.Ellipsis, maxLines = 1)


        }
    }

    @OptIn(ExperimentalCoilApi::class)
    @Composable
    override fun PictureNameView() {
        val painter = rememberImagePainter(
            data = photoPath.ifEmpty {
                R.drawable.ic_frijolrojo
            },
            builder = {
                placeholder(R.drawable.ic_frijolrojo)
            }
        )
        Row() {
            Image(
                painter = painter,
                contentDescription = "photo product",
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(color = Color.White)
            )

            Spacer(modifier = Modifier.padding(4.dp))
            Column {

                Text(
                    text = variety,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.alpha(0.5f)) {
                    Icon(
                        imageVector = if (isVerified) Icons.Filled.CheckCircle else Icons.Filled.Pending,
                        contentDescription = "verification"
                    )
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = if (isVerified) "verified" else "verification pending"
                    )
                }

            }
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    @Composable
    override fun DisplayProduce() {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            PictureNameView()
            DisplayProperty(property = getProperty(Property.Humidity.key), value = humidity)
            DisplayProperty(property = getProperty(Property.Purity.key), value = purity)
            DisplayProperty(property = getProperty(Property.Harvest.key), value = harvest)
            DisplayProperty(property = getProperty(Property.Origin.key), value = origin)
            if (comment.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.elevatedCardElevation(3.dp)
                ) {
                    Text(text = "Details", style = MaterialTheme.typography.labelLarge)
                    Text(text = comment)
                }
            }
        }
    }

    override fun getProduceName(): String {
        return variety
    }

    override fun passesFilter(filters: HashMap<String, PropertyType>): Boolean {
        val humidityPass =
            (filters[Property.Humidity.key] as PropertyType.IntRange).passesFilter(humidity)
        val purityPass =
            (filters[Property.Purity.key] as PropertyType.IntRange).passesFilter(purity)
        val varietyPass =
            (filters[Property.Variety.key] as PropertyType.StringCategory).passesFilter(variety)
        val harvestPass =
            (filters[Property.Harvest.key] as PropertyType.StringCategory).passesFilter(harvest)
        val originPass =
            (filters[Property.Origin.key] as PropertyType.StringCategory).passesFilter(origin)
        val isVerifiedPass =
            (filters[Property.IsVerified.key] as PropertyType.Dichotomous).passesFilter(isVerified)
        return humidityPass && purityPass && varietyPass && harvestPass && originPass && isVerifiedPass
    }

    override fun getOrderingCriterion(property: String): Any {
        return when (property) {
            Property.Humidity.key -> humidity
            Property.Purity.key -> purity
            Property.Harvest.key -> harvest
            Property.Origin.key -> origin
            else -> {
                variety
            }
        }
    }


    override fun getProperties(): HashMap<String, PropertyClass> {
        return propertyMap
    }

    override fun getPropertyByName(name: String): Any? {
        return when (name) {
            Property.UnitOfMeasurement.key -> unitOfMeasurement
            Property.Variety.key -> variety
            Property.Humidity.key -> humidity
            Property.Purity.key -> purity
            Property.Origin.key -> origin
            Property.Harvest.key -> harvest
            Property.PhotoPath.key -> photoPath
            Property.IsVerified.key -> isVerified
            Property.Comment.key -> comment
            else -> {
                null
            }
        }
    }

    override fun copyByName(name: String, value: Any): Frijol {
        return when (name) {
            Property.UnitOfMeasurement.key -> this.copy(unitOfMeasurement = value as String)
            Property.Variety.key -> this.copy(variety = value as String)
            Property.Humidity.key -> this.copy(humidity = value as Int)
            Property.Purity.key -> this.copy(purity = value as Int)
            Property.Origin.key -> this.copy(origin = value as String)
            Property.Harvest.key -> this.copy(harvest = value as String)
            Property.PhotoPath.key -> this.copy(photoPath = value as String)
            Property.IsVerified.key -> this.copy(isVerified = value as Boolean)
            Property.Comment.key -> this.copy(comment = value as String)
            else -> this.copy()
        }
    }


    override fun toProduceDto(): FrijolDto {
        return FrijolDto(
            unitOfMeasurement = unitOfMeasurement,
            variety = variety,
            humidity = humidity,
            purity = purity,
            origin = origin,
            harvest = harvest,
            photoPath = photoPath,
            isVerified = isVerified,
            comment = comment,
        )
    }

    override fun getIsVerified(): Boolean {
        return isVerified
    }

    override fun getUnits(): String {
        return unitOfMeasurement
    }

    override fun toLinkedHashMap(): LinkedHashMap<String, Any> {
        return linkedMapOf(
            Property.UnitOfMeasurement.key to unitOfMeasurement,
            Property.Variety.key to variety,
            Property.Humidity.key to humidity,
            Property.Purity.key to purity,
            Property.Origin.key to origin,
            Property.Harvest.key to harvest,
            Property.PhotoPath.key to photoPath,
            Property.IsVerified.key to isVerified,
            Property.Comment.key to comment
        )
    }

}