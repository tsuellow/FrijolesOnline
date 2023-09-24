package com.ccy.apps.frijolesonline.presentation.publish_lot.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ProductionQuantityLimits
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ccy.apps.frijolesonline.Greeting
import com.ccy.apps.frijolesonline.common.PropertyClass
import com.ccy.apps.frijolesonline.common.PropertyType
import com.ccy.apps.frijolesonline.domain.model.Frijol
import com.ccy.apps.frijolesonline.domain.model.Lot
import com.ccy.apps.frijolesonline.domain.model.PricingData
import com.ccy.apps.frijolesonline.domain.model.Produce
import com.ccy.apps.frijolesonline.presentation.common_components.capitalizeFirstLetter
import com.ccy.apps.frijolesonline.presentation.ui.theme.FrijolesOnlineTheme

@Composable
fun LotView(lot: Lot, produce: Produce, pricingData: PricingData? = null) {

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${lot.produceCategory.capitalizeFirstLetter()} Lot: ",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "" + lot.quantity,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = produce.getUnits(),
                style = MaterialTheme.typography.titleLarge,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .alpha(0.5f)
                    .padding(start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))




        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(intrinsicSize = IntrinsicSize.Max)
        ) {
            PriceView(
                modifier = Modifier
                    .weight(1f),
                price = processPrice(lot.unitPriceOrigin, pricingData),
                units = produce.getUnits(),
                place = "origin"
            )
            lot.destination?.let {
                PriceView(
                    modifier = Modifier
                        .weight(1f)
                        .height(intrinsicSize = IntrinsicSize.Max),
                    price = processPrice(lot.unitPriceDestination, pricingData),
                    units = produce.getUnits(),
                    place = it,
                    minSale = lot.minSale,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
        pricingData?.let {
            Text(
                text = "Includes ${it.variable}C$ commission for each unit.",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .alpha(0.5f)
                    .padding(3.dp)
            )
        }
        lot.destination ?: run {
            Card(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray)
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.padding(start = 8.dp),
                        imageVector = Icons.Filled.Warning,
                        contentDescription = "warning"
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = "Lot has to be picked up at the farm by buyer",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        Card(modifier = Modifier.padding(8.dp)) {
            produce.DisplayProduce()

        }

        Spacer(modifier = Modifier.height(8.dp))
    }


}

fun processPrice(price: Int, pricingData: PricingData?): Int {
    return pricingData?.let {
        price + pricingData.variable
    } ?: price
}

@Composable
fun DisplayProperty(property: PropertyClass, value: Any) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        property.icon?.let {
            Icon(
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary,
                imageVector = it,
                contentDescription = property.name
            )
        } ?: run {
            Spacer(Modifier.width(24.dp))
        }

        Spacer(Modifier.width(4.dp))

        Text(
            text = property.name + ":",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(text = "$value", fontWeight = FontWeight.Bold)

        if (property.propertyType is PropertyType.IntRange) {
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = property.propertyType.unit,
                color = Color.Gray
            )
        }

    }
}

@Composable
fun PriceView(
    modifier: Modifier = Modifier,
    price: Int,
    units: String,
    place: String,
    color: Color = MaterialTheme.colorScheme.tertiaryContainer,
    minSale: Int? = null
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Price in $place", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$price",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "C$/$units",
                    modifier = Modifier.alpha(0.7f),
                    fontStyle = FontStyle.Italic
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            minSale?.let {

                Column(
                    modifier = Modifier.alpha(0.5f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth(), horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ProductionQuantityLimits,
                            contentDescription = "min. order"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "$it")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = units, fontStyle = FontStyle.Italic)

                    }
                    Text(text = "Minimum Order", style = MaterialTheme.typography.labelSmall)
                }


            } ?: run {
                Spacer(modifier = Modifier.height(8.dp))
            }

        }
    }

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview3() {
    FrijolesOnlineTheme {
        LotView(
            lot = Lot(
                destination = "Esteli",
                unitPriceDestination = 45,
                minSale = 34
            ),
            produce = Frijol(),
            pricingData = PricingData()
        )
    }
}