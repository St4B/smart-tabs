package com.quadible.smarttabslist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

@Composable
fun <T> SmartTabsList(
    smartTabsContent: List<T>,
    isTab: (T) -> Boolean,
    smartTab: @Composable (T, Boolean) -> Unit,
    smartItem: @Composable (T) -> Unit,
) {
    val tabs = smartTabsContent.filter { isTab(it) }
    val indexes = smartTabsContent.mapTabs(isTab = isTab)
    val selectedTabIndex = remember { mutableStateOf(0) }
    val verticalListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(smartTabsContent, verticalListState) {
        snapshotFlow { verticalListState.firstVisibleItemIndex }
            .mapNotNull {
                val tabData = smartTabsContent.getOrNull(it)
                indexes[tabData]
            }
            .distinctUntilChanged()
            .collectLatest {
                selectedTabIndex.value = it
            }
    }

    val scrollToItem = scroller(
        verticalListState = verticalListState,
        coroutineScope = coroutineScope,
        smartTabsContent = smartTabsContent,
    )

    Column {
        AnimatedVisibility(
            visible = verticalListState.firstVisibleItemIndex > 0
        ) {
            SmartTabs(
                selectedTabIndex = selectedTabIndex.value,
                onTabSelected = {
                    selectedTabIndex.value = it
                },
                scrollToItem = scrollToItem,
                tabs = tabs,
                smartTab = smartTab,
            )
        }

        SmartTabsItems(
            listState = verticalListState,
            smartTabsContent = smartTabsContent,
            smartItem = smartItem,
        )
    }
}

fun <T> List<T>.mapTabs(isTab: (T) -> Boolean): Map<T, Int> = buildMap {
    var headerIndex = -1 // Tabs are zero-based. -1 means tha no tab exist
    this@mapTabs.forEach {
        if (isTab(it)) {
            headerIndex++
        }
        this[it] = headerIndex
    }
}

@Composable
private fun <T> SmartTabs(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    scrollToItem: (T) -> Unit,
    tabs: List<T>,
    smartTab: @Composable (T, Boolean) -> Unit,
) {
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        edgePadding = 0.dp
    ) {
        tabs.forEachIndexed { index, item ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = {
                    onTabSelected(index)
                    scrollToItem(item)
                }
            ) {
                smartTab(item, selectedTabIndex == index)
            }
        }
    }
}

private fun <T> scroller(
    verticalListState: LazyListState,
    coroutineScope: CoroutineScope,
    smartTabsContent: List<T>,
): (T) -> Unit = {
    coroutineScope.launch {
        val tabIndex = smartTabsContent.indexOf(it)
        verticalListState.animateScrollToItem(index = tabIndex)
    }
}

@Composable
private fun <T> SmartTabsItems(
    listState: LazyListState,
    smartTabsContent: List<T>,
    smartItem: @Composable (T) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = listState
    ) {
        smartTabsContent.forEach {
            item {
                Box {
                    smartItem(it)
                }
            }
        }
    }
}
