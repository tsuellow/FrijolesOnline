package com.ccy.apps.frijolesonline.presentation.lot_detail.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ccy.apps.frijolesonline.common.PropertyClass
import com.ccy.apps.frijolesonline.common.Resource
import com.ccy.apps.frijolesonline.domain.model.Offer
import com.ccy.apps.frijolesonline.domain.model.Offer.Companion.FINCA
import com.ccy.apps.frijolesonline.domain.model.PricingData
import com.ccy.apps.frijolesonline.domain.model.Produce
import com.ccy.apps.frijolesonline.presentation.common_components.PropertyField
import com.ccy.apps.frijolesonline.presentation.lot_detail.LotDetailEvent
import com.ccy.apps.frijolesonline.presentation.lot_detail.LotDetailViewModel
import com.ccy.apps.frijolesonline.presentation.publish_lot.SubmissionState
import com.ccy.apps.frijolesonline.presentation.publish_lot.components.LotView
import com.ccy.apps.frijolesonline.presentation.publish_lot.components.ProcessingButton
import com.ccy.apps.frijolesonline.presentation.ui.theme.FrijolesOnlineTheme
import java.text.DecimalFormat
import java.text.NumberFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LotDetail(
    navController: NavHostController,
    viewModel: LotDetailViewModel = hiltViewModel()
) {


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = "Buy Lot") }, navigationIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "back"
                )
            })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val lotResource = viewModel.lot) {
                is Resource.Success -> {
                    val lot = lotResource.data!!
                    LotView(
                        lot = lot,
                        produce = viewModel.produce,
                        pricingData = viewModel.pricingData
                    )

                    OfferView(
                        pricingData = viewModel.pricingData ?: PricingData(),
                        offer = viewModel.offer,
                        quantityError = viewModel.quantityError,
                        quantityPropertyClass = viewModel.quantityPropertyClass,
                        pricePropertyClass = viewModel.pricePropertyClass,
                        commentPropertyClass = viewModel.commentPropertyClass,
                        destination = viewModel.lot.data?.destination,
                        onDeliveryPlaceChanged = {
                            viewModel.onEvent(
                                LotDetailEvent.SwitchDeliveryPlace(it)
                            )
                        },
                        onPriceEditable = { viewModel.onEvent(LotDetailEvent.MakePriceEditable(it)) },
                        onMsgChanged = { viewModel.onEvent(LotDetailEvent.EditMsg(it)) },
                        onPriceChanged = { viewModel.onEvent(LotDetailEvent.EditPrice(it ?: 0)) },
                        onQuantityChanged = {
                            viewModel.onEvent(
                                LotDetailEvent.EditQuantity(
                                    it ?: 0
                                )
                            )
                        },
                        isPriceEditable = viewModel.isPriceEditable,
                        onSend = { viewModel.onEvent(LotDetailEvent.ShowSubmissionDialog(show = true)) }
                    )

                    if (viewModel.showDialog) {
                        ConfirmationDialog(
                            navController = navController,
                            offer = viewModel.offer,
                            produce = viewModel.produce,
                            submissionState = viewModel.submissionState,
                            onTriggerSubmission = { viewModel.onEvent(LotDetailEvent.SubmitOffer) },
                            onCancelSubmission = {
                                viewModel.onEvent(
                                    LotDetailEvent.ShowSubmissionDialog(
                                        show = false
                                    )
                                )
                            }
                        )
                    }


                }

                else -> {
                    CircularProgressIndicator(modifier = Modifier.padding(8.dp))
                }
            }
        }


    }
}

@Composable
fun OfferView(
    pricingData: PricingData,
    offer: Offer,
    quantityError: String,
    quantityPropertyClass: PropertyClass,
    pricePropertyClass: PropertyClass,
    commentPropertyClass: PropertyClass,
    destination: String? = null,
    isPriceEditable: Boolean = false,
    onQuantityChanged: (Int?) -> Unit,
    onPriceChanged: (Int?) -> Unit,
    onMsgChanged: (String) -> Unit,
    onDeliveryPlaceChanged: (transportedBySeller: Boolean) -> Unit,
    onPriceEditable: (Boolean) -> Unit,
    onSend: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {


        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Make Offer", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(8.dp))

        destination?.let { destinationPlace ->
            Text(
                text = "Transaction Location:",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .alpha(0.5f)
                    .padding(3.dp)
            )
            SwitchDestination(offer, onDeliveryPlaceChanged, destinationPlace)
        }

        PropertyField(
            propertyPair = Pair("_quantity", quantityPropertyClass),
            error = quantityError,
            value = if (offer.quantity == 0) null else offer.quantity,
            onValueChanged = { _key, value ->
                onQuantityChanged(value as Int?)
            }
        )



        PropertyField(
            propertyPair = Pair("_price", pricePropertyClass),
            error = "",
            value = if (offer.price == 0) null else offer.price,
            onValueChanged = { _key, value ->
                onPriceChanged(value as Int?)
            },
            enabled = isPriceEditable
        )

        OutlinedButton(
            modifier = Modifier.animateContentSize(),
            contentPadding = PaddingValues(
                horizontal = 10.dp,
                vertical = 4.dp
            ),
            onClick = { onPriceEditable(!isPriceEditable) }) {
            Text(text = if (isPriceEditable) "Use seller price" else "Propose different price")
        }

        PropertyField(
            propertyPair = Pair("_msg", commentPropertyClass),
            error = "",
            value = offer.comment,
            onValueChanged = { _key, value -> onMsgChanged(value as String) })

        Row(Modifier.fillMaxWidth(0.8f)) {
            Text(
                text = "Intermediation commission:",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .alpha(0.5f)
                    .padding(3.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${pricingData.fixed} C$",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .alpha(0.5f)
                    .padding(3.dp)
            )
        }

        Row(Modifier.fillMaxWidth(0.8f)) {
            Text(
                text = "Total cost:",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .padding(3.dp)
            )
            Spacer(modifier = Modifier.weight(1f))

            val formatter: NumberFormat = DecimalFormat("#,###")
            Text(
                text = "${formatter.format(pricingData.fixed + offer.price * offer.quantity)} C$",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(3.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { onSend() },
            enabled = offer.quantity != 0 && offer.price != 0 && quantityError.isEmpty()
        ) {
            Text(text = "Make Offer")
        }

        Spacer(modifier = Modifier.height(36.dp))


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationDialog(
    navController: NavHostController,
    offer: Offer,
    produce: Produce,
    submissionState: SubmissionState,
    onTriggerSubmission: () -> Unit,
    onCancelSubmission: () -> Unit
) {
    AlertDialog(onDismissRequest = { navController.popBackStack() },
        title = { Text("Confirm Offer") },
        text = {
            Column() {
                Text(
                    text = "Offer Details",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Row {
                    Text(
                        text = "Delivery place:",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(text = offer.deliveryPlace)
                }
                Row {
                    Text(
                        text = "Quantity:",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(text = "${offer.quantity} ${produce.getUnits()}")
                }
                Row {
                    Text(
                        text = "Unit price:",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(text = "${offer.price} C$/${produce.getUnits()}")
                }
                if (offer.comment.isNotEmpty()) {
                    Row {
                        Text(
                            text = "Unit price:",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(text = offer.comment)
                    }
                }
            }

        },
        confirmButton = {
            ProcessingButton(submissionState = submissionState, onClick = onTriggerSubmission)
        },
        dismissButton = {
            Button(onClick = onCancelSubmission) {
                Text(text = "Cancel")
            }
        }
    )
}

@Composable
private fun SwitchDestination(
    offer: Offer,
    onDeliveryPlaceChanged: (transportedBySeller: Boolean) -> Unit,
    destinationPlace: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isOn = offer.deliveryPlace != FINCA
        Row(
            modifier = Modifier
                .weight(1f),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { onDeliveryPlaceChanged(false) }) {
                Text(
                    text = FINCA,
                    modifier = Modifier
                        .alpha(if (isOn) 0.33f else 1f),
                    color = if (isOn) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

        }
        Switch(
            modifier = Modifier.padding(horizontal = 2.dp),
            checked = isOn,
            onCheckedChange = { onDeliveryPlaceChanged(it) },
            colors = SwitchDefaults.colors(
                uncheckedThumbColor = MaterialTheme.colorScheme.tertiaryContainer,
                uncheckedTrackColor = MaterialTheme.colorScheme.tertiary,
                uncheckedBorderColor = MaterialTheme.colorScheme.tertiary,
                uncheckedIconColor = MaterialTheme.colorScheme.onTertiaryContainer
            ),
            thumbContent = {
                Icon(
                    imageVector = if (isOn) Icons.Default.LocalShipping else Icons.Default.House,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                    contentDescription = "ship"
                )
            }
        )
        Row(
            modifier = Modifier
                .weight(1f),
            horizontalArrangement = Arrangement.Start
        ) {
            TextButton(
                onClick = { onDeliveryPlaceChanged(true) }
            ) {
                Text(
                    text = destinationPlace,
                    modifier = Modifier
                        .alpha(if (!isOn) 0.33f else 1f),
                    textAlign = TextAlign.Left,
                    color = if (!isOn) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview3() {
    FrijolesOnlineTheme {
        SwitchDestination(
            offer = Offer(deliveryPlace = "ESTELI"),
            onDeliveryPlaceChanged = {},
            destinationPlace = "ESTELI"
        )
    }
}