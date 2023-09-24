package com.ccy.apps.frijolesonline.presentation.lot_list.components

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ccy.apps.frijolesonline.R
import com.ccy.apps.frijolesonline.common.*
import com.ccy.apps.frijolesonline.domain.model.Frijol
import com.ccy.apps.frijolesonline.domain.model.Lot
import com.ccy.apps.frijolesonline.presentation.common_components.*
import com.ccy.apps.frijolesonline.presentation.lot_list.Event
import com.ccy.apps.frijolesonline.presentation.lot_list.LotListViewModel
import com.ccy.apps.frijolesonline.presentation.ui.theme.FrijolesOnlineTheme
import com.ccy.apps.frijolesonline.pullrefresh_placeholder.PullRefreshIndicator
import com.ccy.apps.frijolesonline.pullrefresh_placeholder.pullRefresh
import com.ccy.apps.frijolesonline.pullrefresh_placeholder.rememberPullRefreshState
import com.ccy.apps.frijolesonline.util.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
fun LotList(
    navController: NavController,
    viewModel: LotListViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                viewModel.lotOrderCriteria.forEach { (name, property) ->
                    NavigationDrawerItem(
                        modifier = Modifier.padding(4.dp),
                        label = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = property.name)
                                if (viewModel.lotOrdering.value.contentEquals(name)) {
                                    Icon(
                                        imageVector = Icons.Filled.Sort,
                                        contentDescription = "descending",
                                        modifier = if (viewModel.orderType.value is OrderType.Ascending) Modifier.scale(
                                            1f,
                                            -1f
                                        ) else Modifier
                                    )
                                }
                            }
                        },
                        icon = {
                            property.icon?.let {
                                Icon(
                                    imageVector = it,
                                    contentDescription = "descending"
                                )
                            }
                        },
                        selected = viewModel.lotOrdering.value.contentEquals(name),
                        onClick = {
                            viewModel.onEvent(Event.ReorderEvent(name))
                        }
                    )
                }

            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Lotes de Frijoles")
                    },
                    navigationIcon = {
                        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Sort,
                                contentDescription = "sort order"
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { viewModel.onEvent(Event.RemoveAllFilters) },
                            enabled = viewModel.areFiltersActive()
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterAltOff,
                                contentDescription = "filter",
                            )
                        }
                        IconButton(onClick = { viewModel.onEvent(Event.RefreshEvent) }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "refresh"
                            )
                        }
                    },
                    //colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
                )
            }
        ) { padding ->

            val refreshState = rememberPullRefreshState(
                refreshing = viewModel.filteredLotList.value is Resource.Loading,
                onRefresh = { viewModel.onEvent(Event.RefreshEvent) })
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FilterRow(
                    filterMap = viewModel.filterArray.toMap(),
                    orderBy = viewModel.lotOrdering.value,
                    orderType = viewModel.orderType.value,
                    doOnFilterSet = { viewModel.onEvent(Event.FilterEvent(it)) })
                when (val list = viewModel.filteredLotList.value) {
                    is Resource.Error -> {
                        Column(Modifier.fillMaxWidth()) {
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = "Error",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelLarge
                            )
                            list.message?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }

                    else -> {
                        Box(
                            modifier = Modifier.pullRefresh(refreshState)
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .padding()
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Top
                            ) {
                                items(list.data ?: emptyList()) { lot ->
                                    LotItem(
                                        lot = lot,
                                        onClick = { id ->
                                            navController.navigate(
                                                Screen.LotDetail.withId(root = lot.produceCategory,id=id)
                                            )
                                        })
                                }
                            }

                            PullRefreshIndicator(
                                list is Resource.Loading,
                                refreshState,
                                modifier = Modifier
                                    .padding(20.dp)
                                    .align(Alignment.TopCenter)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterRow(
    filterMap: Map<String, PropertyClass>,
    orderBy: String,
    orderType: OrderType,
    doOnFilterSet: (Pair<String, PropertyType>) -> Unit
) {
    var propertyPair by remember {
        mutableStateOf(
            Pair(
                "test", PropertyClass(
                    name = "test",
                    description = "test",
                    propertyType = PropertyType.IntRange()
                )
            )
        )
    }
    var isExpanded by remember {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope { Dispatchers.Main }
    Column() {
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(filterMap.map { Pair(it.key, it.value) }) { filter ->
                AssistChip(
                    modifier = Modifier.padding(4.dp),
                    leadingIcon = {
                        if (filter.second.propertyType.isActive()) {
                            Icon(
                                imageVector = Icons.Filled.FilterAlt,
                                contentDescription = "active"
                            )
                        }
                    },
                    border = if (isExpanded == filter.first) {
                        AssistChipDefaults.assistChipBorder(borderWidth = 0.dp)
                    } else {
                        if (filter.second.propertyType.isActive()) {
                            AssistChipDefaults.assistChipBorder(borderWidth = 3.dp)
                        } else {
                            AssistChipDefaults.assistChipBorder()
                        }
                    },
                    colors = if (isExpanded == filter.first) {
                        AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                        )
                    } else {
                        AssistChipDefaults.assistChipColors()
                    },
                    trailingIcon = {
                        if (filter.first.contentEquals(orderBy)) {
                            Icon(
                                tint = Color.LightGray,
                                imageVector = Icons.Filled.Sort,
                                contentDescription = "sorted by",
                                modifier = if (orderType is OrderType.Ascending) Modifier.scale(
                                    1f,
                                    -1f
                                ) else Modifier
                            )
                        }
                    },
                    elevation = if (isExpanded == filter.first) {
                        AssistChipDefaults.assistChipElevation(2.dp)
                    } else {
                        AssistChipDefaults.assistChipElevation()
                    },
                    onClick = {
                        isExpanded = if (isExpanded == filter.first) "" else filter.first
                        if (propertyPair.second != filter.second) {
                            scope.launch {
                                delay(200)
                                isExpanded = filter.first
                            }
                        }
                        propertyPair = filter.copy()
                    },
                    label = { Text(text = filter.second.name) }
                )

            }

        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                )
        ) {
            if (isExpanded.isNotEmpty()) {
                FilterCard(pair = propertyPair,
                    doOnFilterSet = {
                        doOnFilterSet(it)
                        isExpanded = ""
                    }
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterCard(
    pair: Pair<String, PropertyClass>,
    doOnFilterSet: (Pair<String, PropertyType>) -> Unit
) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = pair.second.description)
        Spacer(Modifier.height(12.dp))

        val filterKey by remember {
            mutableStateOf(pair.first)
        }
        var filter by remember {
            mutableStateOf(pair.second.propertyType)
        }

        when (filter) {
            is PropertyType.IntRange -> {
                var errorMin by remember {
                    mutableStateOf("")
                }
                var errorMax by remember {
                    mutableStateOf("")
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    with(filter as PropertyType.IntRange) {
                        Spacer(modifier = Modifier.weight(1f))
                        OutlinedTextFieldValidation(
                            modifier = Modifier.weight(5f),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            label = { Text(text = "Min.") },
                            error = errorMin,
                            value = (if (this.min == null) "" else "" + this.min),
                            onValueChange = { typed ->
                                val newVal = typed.filter { it.isDigit() }.toIntOrNull()
                                errorMin = if (this.inRange(newVal)) {
                                    ""
                                } else {
                                    "value must be within ${this.minRange} and ${this.maxRange}"
                                }
                                filter = this.copy(min = newVal)
                            }
                        )
                        Spacer(modifier = Modifier.weight(0.5f))
                        OutlinedTextFieldValidation(
                            modifier = Modifier.weight(5f),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            label = { Text(text = "Max.") },
                            error = errorMax,
                            value = (if (this.max == null) "" else "" + this.max),
                            onValueChange = { typed ->
                                val newVal = typed.filter { it.isDigit() }.toIntOrNull()
                                errorMax = if (this.inRange(newVal)) {
                                    ""
                                } else {
                                    "value must be within ${this.minRange} and ${this.maxRange}"
                                }
                                filter = this.copy(max = newVal)
                            }
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }

                }
            }

            is PropertyType.DateTime -> {
                Row(modifier = Modifier.fillMaxWidth()) {
                    with(filter as PropertyType.DateTime) {
                        Spacer(modifier = Modifier.weight(1f))
                        OutlinedTextFieldValidationDatePicker(
                            modifier = Modifier.weight(9f),
                            label = { Text(text = "from") },
                            readOnly = true,
                            value = (if (this.from == null) "" else "" + this.from.toDate()!!
                                .toReadableDateString()),
                            onValueChange = { dateString ->
                                filter = (filter as PropertyType.DateTime).copy(from = dateString)
                            }
                        )
                        Spacer(modifier = Modifier.weight(0.5f))
                        OutlinedTextFieldValidationDatePicker(
                            modifier = Modifier.weight(9f),
                            label = { Text(text = "to") },
                            readOnly = true,
                            value = (if (this.to == null) "" else "" + this.to.toDate()!!
                                .toReadableDateString()),
                            onValueChange = { dateString ->
                                filter = (filter as PropertyType.DateTime).copy(to = dateString)
                            }
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            is PropertyType.Dichotomous -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    with(filter as PropertyType.Dichotomous) {
                        Spacer(modifier = Modifier.weight(1f))
                        Checkbox(
                            checked = this.yes,
                            onCheckedChange = {
                                filter = (filter as PropertyType.Dichotomous).copy(yes = it)
                            }
                        )
                        Text(
                            text = "yes",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(0.5f))
                        Checkbox(
                            checked = this.no,
                            onCheckedChange = {
                                filter = (filter as PropertyType.Dichotomous).copy(no = it)
                            }
                        )
                        Text(
                            text = "no",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            is PropertyType.FreeText -> {
                Row(modifier = Modifier.fillMaxWidth()) {
                    with(filter as PropertyType.FreeText) {
                        Spacer(modifier = Modifier.weight(1f))
                        OutlinedTextFieldValidation(
                            modifier = Modifier.weight(12f),
                            label = { Text(text = "search text") },
                            value = this.text ?: "",
                            onValueChange = {
                                filter = (filter as PropertyType.FreeText).copy(
                                    text = it
                                )
                            }
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

            }

            is PropertyType.StringCategory -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    with(filter as PropertyType.StringCategory) {
                        val newList = ArrayList((this@with).list)
                        var searchText by remember {
                            mutableStateOf("")
                        }
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "search"
                                )
                            })
                        Spacer(Modifier.height(8.dp))
                        Card(
                            modifier = Modifier
                                .padding(12.dp)
                                .heightIn(min = 0.dp, max = 200.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
                                ),
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                items((filter as PropertyType.StringCategory).listRange.filter {
                                    it.lowercase().removeNonSpacingMarks()
                                        .contains(searchText.removeNonSpacingMarks().lowercase())
                                }) { listItem ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Spacer(modifier = Modifier.width(20.dp))
                                        Checkbox(
                                            checked = listItem in (filter as PropertyType.StringCategory).list,
                                            onCheckedChange = {
                                                if (it) {
                                                    newList.add(listItem)
                                                } else {
                                                    newList.remove(listItem)
                                                }
                                                filter =
                                                    (filter as PropertyType.StringCategory).copy(
                                                        list = newList
                                                    )
                                            }
                                        )
                                        Text(text = listItem, overflow = TextOverflow.Ellipsis)
                                        Spacer(modifier = Modifier.weight(0.5f))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            OutlinedButton(
                modifier = Modifier.weight(4f),
                onClick = {
                    filter = filter.removeFilter()
                },
            ) {
                Text(text = "Clear")
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier.weight(4f),
                onClick = {
                    doOnFilterSet(Pair(filterKey, filter))
                }
            ) {
                Text(text = "Apply")
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))
    }

}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun LotItem(lot: Lot, onClick: (pushId: String) -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick(lot.lotId) },
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Surface(modifier = Modifier.fillMaxWidth()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                    //horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    lot.toProduce().PictureNameView()

                    Spacer(modifier = Modifier.width(8.dp))

                    Card(
                        modifier = Modifier.weight(2f),
                        elevation = CardDefaults.cardElevation(3.dp),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocalShipping,
                                contentDescription = "Quantity",
                                tint = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = "${lot.quantity}qq",
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }
                    Spacer(modifier = Modifier.width(8.dp))

                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(Modifier.weight(1f)) {
                        lot.toProduce().GetEssentials()

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Text(
                                text = "published: ",
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(Modifier.padding(2.dp))
                            Text(
                                text = lot.dateUpdated,
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                                style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic)
                            )
                        }

                    }
                    Spacer(Modifier.weight(0.1f))
                    Column(horizontalAlignment = Alignment.End) {
                        val price =
                            if (lot.unitPriceDestination == 0) lot.unitPriceOrigin else lot.unitPriceDestination
                        val place = if (lot.unitPriceDestination == 0) "FINCA" else lot.destination
                        Text(
                            text = "$price C$/qq",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1
                        )
                        Text(
                            text = "precio en $place",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary,
                            maxLines = 1
                        )
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemPreview() {
    FrijolesOnlineTheme {
        val lot =
            Lot(
                unitPriceDestination = 3600,
                produce = Frijol(origin = "Esteli - Pueblo Nuevo y mucho mass").toLinkedHashMap()
            )
        LotItem(lot = lot)
    }
}