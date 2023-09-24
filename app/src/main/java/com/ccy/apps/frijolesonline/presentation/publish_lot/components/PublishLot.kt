package com.ccy.apps.frijolesonline.presentation.publish_lot.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextMotion.Companion.Animated
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ccy.apps.frijolesonline.common.PropertyClass
import com.ccy.apps.frijolesonline.common.PropertyType
import com.ccy.apps.frijolesonline.domain.model.Frijol
import com.ccy.apps.frijolesonline.domain.model.Lot
import com.ccy.apps.frijolesonline.presentation.common_components.CreateFromPropertyClasses
import com.ccy.apps.frijolesonline.presentation.common_components.PropertyField
import com.ccy.apps.frijolesonline.presentation.publish_lot.PublishLotEvent
import com.ccy.apps.frijolesonline.presentation.publish_lot.PublishLotViewModel
import com.ccy.apps.frijolesonline.presentation.publish_lot.SubmissionState
import com.ccy.apps.frijolesonline.presentation.ui.theme.FrijolesOnlineTheme
import com.ccy.apps.frijolesonline.util.Screen
//import com.google.accompanist.navigation.animation.AnimatedNavHost
//import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun PublishLot(
    mainNavController: NavHostController,
    viewModel: PublishLotViewModel = hiltViewModel()
) {
    //Tabs
    val PRODUCE = "Produce"
    val LOT = "Lot"
    val REVIEW = "Review/Publish"

    var tabState by rememberSaveable {
        mutableIntStateOf(0)
    }
    val navController: NavHostController = viewModel.navController//
    //val animatedNavController= rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = Unit) {
        viewModel.snackbarState.collect {
            if (it.isNotEmpty()) {
                snackbarHostState.showSnackbar(message = it, withDismissAction = true)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = "Publish Lot")
            })
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .padding(padding)
                .imePadding()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TabRow(selectedTabIndex = tabState) {

                    Tab(
                        selected = tabState == 0,
                        onClick = {
                            viewModel.onEvent(
                                PublishLotEvent.NavigateEvent(PublishScreen.ProduceScreen)
                            )
                        },
                        text = {
                            Row(verticalAlignment = CenterVertically) {
                                Text(
                                    text = PRODUCE,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Icon(
                                    modifier = Modifier.padding(start = 8.dp),
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "next"
                                )
                            }
                        }
                    )
                    Tab(
                        selected = tabState == 1,
                        onClick = {
                            viewModel.onEvent(PublishLotEvent.NavigateEvent(PublishScreen.LotScreen))
                        },
                        text = {
                            Row(verticalAlignment = CenterVertically) {
                                Text(
                                    text = LOT,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Icon(
                                    modifier = Modifier.padding(start = 8.dp),
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "next"
                                )
                            }
                        }
                    )
                    Tab(
                        selected = tabState == 2,
                        onClick = {
                            viewModel.onEvent(PublishLotEvent.NavigateEvent(PublishScreen.ReviewScreen))
                        },
                        text = {
                            Row(verticalAlignment = CenterVertically) {
                                Text(
                                    text = REVIEW,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    )


                }


                //AnimatedNavHost(
                NavHost(
                    navController = navController,
                    startDestination = PublishScreen.ProduceScreen.route,
//                    enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
//
//                    exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) },
//                    popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) },
//                    popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) }
                ) {
                    composable(route = PublishScreen.ProduceScreen.route,
                        enterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) }
                    ) {
                        tabState = 0
                        CreateProduce(navController = navController, viewModel = viewModel)
                    }
                    composable(route = PublishScreen.LotScreen.route,
                        enterTransition = {
                            if (tabState > 1) slideInHorizontally(initialOffsetX = { -1000 }) else slideInHorizontally(
                                initialOffsetX = { 1000 })
                        },
                        exitTransition = {
                            if (tabState > 1) slideOutHorizontally(targetOffsetX = { -1000 }) else slideOutHorizontally(
                                targetOffsetX = { -1000 })
                        }
                    ) {
                        tabState = 1
                        CreateLot(viewModel = viewModel)
                    }
                    composable(route = PublishScreen.ReviewScreen.route,
                        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) }
                    ) {
                        tabState = 2
                        ReviewLot(viewModel = viewModel, mainNavController = mainNavController)
                    }
                }
            }


        }

    }
}

sealed class PublishScreen(val route: String) {
    object ProduceScreen : PublishScreen(route = Screen.PublishLot.route + "/produce")
    object LotScreen : PublishScreen(route = Screen.PublishLot.route + "/lot")
    object ReviewScreen : PublishScreen(route = Screen.PublishLot.route + "/review")
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun CreateLot(viewModel: PublishLotViewModel) {
    CreateLotFromPropertyClasses(
        propertyMap = viewModel.lotPropertyMap,
        valueMap = viewModel.lotMap,
        errorMap = viewModel.lotErrorMap,
        onPropertyEdited = { key, value ->
            viewModel.onEvent(
                PublishLotEvent.EditLotMapEvent(
                    key,
                    value
                )
            )
        },
        onErrorFound = { key, error ->
            viewModel.onEvent(
                PublishLotEvent.LotErrorEvent(
                    key,
                    error
                )
            )
        },
        submit = {
            viewModel.onEvent(PublishLotEvent.LotSubmittedEvent)
        },
        isDeliverable = viewModel.isLotDeliverable.value,
        setIsDeliverable = { viewModel.onEvent(PublishLotEvent.SetIsDeliverable(deliverable = it)) },
        checkInputs = viewModel.checkInputsLot.value
    )
}

@Composable

fun CreateLotFromPropertyClasses(
    modifier: Modifier = Modifier,
    propertyMap: LinkedHashMap<String, PropertyClass>,
    valueMap: SnapshotStateMap<String, Any?>,
    errorMap: SnapshotStateMap<String, String>,
    submit: () -> Unit,
    onPropertyEdited: (key: String, value: Any?) -> Unit,
    onErrorFound: (key: String, error: String) -> Unit,
    isDeliverable: Boolean = false,
    setIsDeliverable: (Boolean) -> Unit = {},
    checkInputs: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(propertyMap.filter {
                it.key in listOf(
                    Lot.Features.Property.Quantity.key,
                    Lot.Features.Property.UnitPriceOrigin.key
                )
            }.map { Pair(it.key, it.value) }) { property ->
                PropertyField(
                    propertyPair = property.copy(second = property.second),
                    error = errorMap[property.first]!!,
                    value = valueMap[property.first],
                    onValueChanged = { key, value ->
                        onPropertyEdited(key, value)
                        if (checkInputs) {
                            onErrorFound(key, property.second.hasError(value.toString()))
                        }
                    })
            }

            item {
                PropertyField(
                    propertyPair = Pair(
                        "isDeliverable",
                        PropertyClass(
                            name = "I am able to deliver product",
                            propertyType = PropertyType.Dichotomous(),
                            description = "can product be delivered by producer"
                        )
                    ),
                    error = "",
                    value = isDeliverable,
                    onValueChanged = { key, value ->
                        setIsDeliverable(value as Boolean)
                    })
            }
            val ifDeliverable = listOf(
                Lot.Features.Property.Destination.key,
                Lot.Features.Property.UnitPriceDestination.key,
                Lot.Features.Property.MinimumSale.key,
            )
            items(
                propertyMap.filter {
                    it.key in ifDeliverable
                }.toSortedMap(compareBy(ifDeliverable::indexOf))
                    .map { Pair(it.key, it.value) }) { property ->
                PropertyField(
                    enabled = isDeliverable,
                    propertyPair = property.copy(second = property.second.copy(isRequired = isDeliverable)),
                    error = errorMap[property.first]!!,
                    value = valueMap[property.first],
                    onValueChanged = { key, value ->
                        onPropertyEdited(key, value)
                        if (checkInputs) {
                            onErrorFound(key, property.second.hasError(value.toString()))
                        }
                    })
            }
            item {
                PropertyField(
                    propertyPair = Pair(
                        Lot.Features.Property.Observations.key,
                        propertyMap[Lot.Features.Property.Observations.key]!!
                    ),
                    error = "",
                    value = valueMap[Lot.Features.Property.Observations.key] ?: "",
                    onValueChanged = { key, value ->
                        onPropertyEdited(key, value)
                    })
            }
            item {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(onClick = {
                    submit()
                }) {
                    Text(modifier = Modifier.padding(start = 8.dp), text = "next")
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "next"
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun ReviewLot(viewModel: PublishLotViewModel, mainNavController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var showDialog by remember { mutableStateOf(false) }

        LotView(lot = viewModel.lot.value, produce = viewModel.produce)
        Spacer(modifier = Modifier.height(12.dp))
        ProcessingButton(
            viewModel.submissionState.value,
            onClick = { viewModel.onEvent(PublishLotEvent.PublishEvent) })
        Spacer(modifier = Modifier.height(12.dp))

        LaunchedEffect(viewModel.submissionState.value) {
            if (viewModel.submissionState.value is SubmissionState.Success) {
                delay(1000)
                showDialog = true
            }
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { mainNavController.popBackStack() },
                title = { Text("Success!") },
                text={ Text(text = "Lot was published successfully and will son be reviewed.")},
                confirmButton = {
                    Button(onClick = {
                        showDialog=false
                        mainNavController.popBackStack()
                    }) {
                        Text(
                            text = "conclude process"
                        )
                    }
                })
        }
    }

}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun CreateProduce(navController: NavController, viewModel: PublishLotViewModel) {
    CreateFromPropertyClasses(
        propertyMap = viewModel.propertyMap,
        produceMap = viewModel.produceMap,
        errorMap = viewModel.produceErrorMap,
        onPropertyEdited = { key, value ->
            viewModel.onEvent(
                PublishLotEvent.EditProduceMapEvent(
                    key,
                    value
                )
            )
        },
        onErrorFound = { key, error ->
            viewModel.onEvent(
                PublishLotEvent.ProduceErrorEvent(
                    key,
                    error
                )
            )
        },
        submit = {
            viewModel.onEvent(PublishLotEvent.ProduceSubmittedEvent)
        },
        checkInputs = viewModel.checkInputsProduce.value
    )
}

@Composable
fun ProcessingButton(submissionState: SubmissionState, onClick: () -> Unit) {
    val color by animateColorAsState(
        when (submissionState) {
            SubmissionState.Idle -> MaterialTheme.colorScheme.primary
            SubmissionState.Processing -> MaterialTheme.colorScheme.primary
            is SubmissionState.Success -> Color.Green//.copy(alpha = 0.6f)
            is SubmissionState.Failed -> MaterialTheme.colorScheme.error//.copy(alpha = 0.6f)
        }
    )

    AnimatedContent(targetState = submissionState) {
        Button(

            colors = ButtonDefaults.buttonColors(containerColor = color),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            onClick = onClick
        ) {

            Crossfade(modifier = Modifier.animateContentSize(animationSpec = tween(300)),targetState = it) {
                var resultReceived by remember {
                    mutableStateOf(false)
                }
                val rotationDeg by animateFloatAsState(
                    targetValue = if (resultReceived) 360f else 0f,
                    animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
                )
                when (it) {
                    SubmissionState.Idle -> {
                        Icon(imageVector = Icons.Filled.Upload, contentDescription = "submit")
                    }

                    SubmissionState.Processing -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeCap = StrokeCap.Round,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    is SubmissionState.Success -> {
                        Icon(
                            modifier = Modifier.rotate(rotationDeg),
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "submit"
                        )
                        resultReceived = true
                    }

                    is SubmissionState.Failed -> {
                        Icon(
                            modifier = Modifier.rotate(rotationDeg),
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "submit"
                        )
                        resultReceived = true
                    }
                }

            }

            Spacer(modifier = Modifier.width(8.dp))

            Crossfade(targetState = it) {
                when (it) {
                    SubmissionState.Idle -> {
                        Text(text = "Publish")
                    }

                    SubmissionState.Processing -> {
                        Text(text = "Processing...")
                    }

                    is SubmissionState.Success -> {
                        Text(text = "Successful!")

                    }

                    is SubmissionState.Failed -> {
                        Text(text = "Failed!")
                    }
                }
            }

        }
    }



}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FrijolesOnlineTheme {
        Column() {
            ProcessingButton(submissionState = SubmissionState.Idle, onClick = {})
            ProcessingButton(submissionState = SubmissionState.Processing, onClick = {})
            ProcessingButton(submissionState = SubmissionState.Success(""), onClick = {})
            ProcessingButton(submissionState = SubmissionState.Failed("error"), onClick = {})
        }

    }
}