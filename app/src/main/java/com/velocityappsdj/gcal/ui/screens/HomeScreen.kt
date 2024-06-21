package com.velocityappsdj.gcal.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.velocityappsdj.gcal.Screen
import com.velocityappsdj.gcal.network.model.response.Event
import com.velocityappsdj.gcal.ui.actions.HomeScreenAction
import com.velocityappsdj.gcal.ui.state.CalendarDay
import com.velocityappsdj.gcal.ui.state.HomeState
import com.velocityappsdj.gcal.ui.theme.LIGHTGRAY
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeScreenAction) -> Unit,
    navigateToEvent: (Event) -> Unit
) {
    val pageCount = 12 * 100
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp.dp
    val scope = rememberCoroutineScope()
    Scaffold() { paddingValue ->
        Column(modifier = Modifier.padding(paddingValue)) {
            val monthFormatter = DateTimeFormatter.ofPattern("MMM yyyy")
            val dayFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
            val pagerState =
                rememberPagerState(pageCount = { pageCount }, initialPage = pageCount / 2)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp)
            ) {


                IconButton(onClick = {
                    scope.launch {
                        onAction(HomeScreenAction.GoToPreviousMonth)
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                }) {
                    Icon(Icons.Filled.KeyboardArrowLeft, contentDescription = null)
                }
                Text(
                    text = if (state.currentMonth == null) "Loading" else monthFormatter.format(
                        state.currentMonth
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = {
                    scope.launch {
                        onAction(HomeScreenAction.GoToNextMonth)
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }) {
                    Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null)
                }
            }
            HorizontalPager(
                modifier = Modifier.height(screenHeight / 2),
                state = pagerState,
                userScrollEnabled = false,
                beyondBoundsPageCount = 2
            ) {
                if (state.isLoading)
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                else
                    LazyVerticalGrid(columns = GridCells.Fixed(7)) {
                        val calendarDays = state.calendarDays
                        calendarDays?.let {
                            it.forEachIndexed { index, item ->
                                item {
                                    CalendarGridItem(
                                        calendarDay = item,
                                        isTopRow = index < 7,
                                        isSelectedDate = state.selectedDate == item,
                                        isFirstColumn = index % 7 == 0,
                                        isLastRow = index > (it.size - 7)
                                    ) {
                                        onAction(it)
                                    }
                                }
                            }
                        }
                    }
            }

            Column(modifier = Modifier.height(height = screenHeight / 2)) {


                if (state.selectedDate != null)
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 8.dp)
                    ) {
                        Text(
                            text = dayFormatter.format(state.selectedDate?.localDateTime),
                            style = MaterialTheme.typography.titleMedium
                        )

                    }

                state.selectedDate?.let { calendarDay ->
                    if (calendarDay.events.isNotEmpty())
                        LazyColumn {
                            items(calendarDay.events.size) { index ->
                                EventItem(event = calendarDay.events[index]) {
                                    navigateToEvent(it)
                                }
                            }
                        }
                    else
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 8.dp)
                        ) {
                            Text(text = "No events")
                        }

                }
            }
        }

    }
}

@Composable
fun EventItem(
    modifier: Modifier = Modifier,
    event: Event,
    navigateToEvent: (Event) -> Unit
) {
    Card(modifier = Modifier
        .padding(8.dp)
        .clickable {
            navigateToEvent(event)
        }) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = event.summary ?: "No title", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = getEventStartEndTime(event), style = MaterialTheme.typography.bodyMedium)
        }
    }
}


private fun getEventStartEndTime(event: Event): String {
    val dateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    val startDateTime = dateFormatter.parse(event.start.dateTime)
    val endDateTime = dateFormatter.parse(event.end?.dateTime)
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    return "${timeFormatter.format(startDateTime)} - ${timeFormatter.format(endDateTime)}"
}


@Composable
fun CalendarGridItem(
    modifier: Modifier = Modifier,
    calendarDay: CalendarDay,
    isTopRow: Boolean = false,
    isSelectedDate: Boolean = false,
    isFirstColumn: Boolean = false,
    isLastRow: Boolean = false,
    onAction: (HomeScreenAction) -> Unit
) {
    val circleColor = if (isSelectedDate) MaterialTheme.colorScheme.tertiary else Color.Transparent
    Row(modifier = Modifier
        .height(IntrinsicSize.Min)
        .clickable {
            onAction(HomeScreenAction.ChangeSelectedDate(calendarDay))
        }) {
        if (!isFirstColumn)
            Divider(
                thickness = 0.5.dp, modifier = Modifier
                    .fillMaxHeight()
                    .width(
                        0.5.dp
                    ), color = LIGHTGRAY
            )
        Column(
            modifier = modifier.height(if (isTopRow) 40.dp else 55.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Divider(thickness = 0.5.dp, color = LIGHTGRAY)
            if (calendarDay.date == "0")
                Box {

                }
            else
                Text(
                    style = MaterialTheme.typography.bodyMedium.copy(),
                    text = calendarDay.date,
                    modifier = modifier
                        .padding(top = 8.dp)
                        .drawBehind {
                            drawCircle(color = circleColor, radius = 25f)
                        }
                )

            val dots = calendarDay.events.size % 3
            Spacer(modifier = Modifier.weight(1.0f))
            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                repeat(dots) {
                    Dot()
                }
            }
            Divider(thickness = 0.5.dp, color = LIGHTGRAY)

        }

    }


}


@Composable
private fun Dot() {
    val color = MaterialTheme.colorScheme.primary

    Canvas(
        modifier = Modifier
            .padding(2.dp)
            .size(5.dp),
        onDraw = {
            drawCircle(
                color = color,
                radius = 5f,
                style = Fill
            )

        }
    )
}


/*@Composable
fun CalendarWithEvents(events: List<Event>) {
    // MutableState for current month
    val currentMonth = remember { mutableStateOf(Calendar.getInstance()) }

    // MutableState for selected date events
    val selectedDateEvents = remember { mutableStateOf<List<Event>>(listOf()) }

    // UI Layout
    HorizontalPager(
        count = Int.MAX_VALUE,
        modifier = Modifier.fillMaxSize(),
        state = rememberPagerState(initialPage = Int.MAX_VALUE / 2),
        verticalAlignment = Alignment.Top
    ) { page ->
        val month = currentMonth.value.clone() as Calendar
        month.add(Calendar.MONTH, page - Int.MAX_VALUE / 2)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Display month and year
            Text(
                text = "${
                    month.getDisplayName(
                        Calendar.MONTH,
                        Calendar.LONG,
                        Locale.getDefault()
                    )
                } ${month.get(Calendar.YEAR)}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Calendar controls
            CalendarControls(currentMonth, page)

            // Calendar grid with events
            val daysInMonth = month.getActualMaximum(Calendar.DAY_OF_MONTH)
            val days = (1..daysInMonth).toList()
            CalendarGrid(events, month, selectedDateEvents)

            // Display events for selected date at the bottom
            EventList(selectedDateEvents.value)
        }
    }
}*/
