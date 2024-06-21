package com.velocityappsdj.gcal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velocityappsdj.gcal.common.Result
import com.velocityappsdj.gcal.domain.GetCalendarDaysUseCase
import com.velocityappsdj.gcal.domain.GetEventsGroupedUseCase
import com.velocityappsdj.gcal.domain.GetEventsForMonthUseCase
import com.velocityappsdj.gcal.network.model.response.Event
import com.velocityappsdj.gcal.ui.actions.HomeScreenAction
import com.velocityappsdj.gcal.ui.state.CalendarDay
import com.velocityappsdj.gcal.ui.state.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getEventsForMonthUseCase: GetEventsForMonthUseCase,
    private val getEventsGroupedUseCase: GetEventsGroupedUseCase,
    private val getCalendarDays: GetCalendarDaysUseCase
) :
    ViewModel() {
    private val dayFormatter = DateTimeFormatter.ofPattern("d")
    private val monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM")

    private val _state = MutableStateFlow(HomeState(isLoading = true))
    val state = _state


    private val allEvents: HashMap<String, List<Event>> = hashMapOf()



    fun getEventsInitial() {
        viewModelScope.launch {
            when (val result = getEventsForMonthUseCase()) {
                is Result.Error -> _state.value =
                    HomeState(error = result.error.name, isLoading = false)

                is Result.Success -> {
                    updateAllEvents(result.data)
                    setInitialState();
                }
            }
        }

    }

    fun goToPreviousMonth() {
        val prevMonth = _state.value.currentMonth?.minusMonths(1)
        val prevYearMonth = getCurrentMonthString(prevMonth)
        viewModelScope.launch {
            setMonthEvents(prevYearMonth, prevMonth)
        }
    }

    fun goToNextMonth() {
        val nextMonth = _state.value.currentMonth?.plusMonths(1)
        val nextYearMonth = getCurrentMonthString(nextMonth)
        viewModelScope.launch {
            setMonthEvents(nextYearMonth, nextMonth)
        }
    }

    private suspend fun setMonthEvents(
        yearMonth: String?,
        month: LocalDateTime?
    ) {
        if (allEvents[yearMonth] != null) {
            val currentMonthEvents = getEventsGroupedUseCase(allEvents[yearMonth], dayFormatter)
            _state.update {
                it.copy(
                    isLoading = false, error = null,
                    currentMonth = month,
                    yearMonth = yearMonth,
                    currentMonthEvents = currentMonthEvents,
                    calendarDays = getCalendarDays(month, currentMonthEvents)
                )
            }
        } else {
            _state.update {
                it.copy(
                    isLoading = true, error = null,
                    currentMonth = month,
                    yearMonth = yearMonth,
                )
            }

            getMonthEventsFromUseCase(month)
            changeMonth(yearMonth)
        }
    }

    private suspend fun getMonthEventsFromUseCase(month: LocalDateTime?) {
        when (val result = getEventsForMonthUseCase(month)) {
            is Result.Error -> _state.value =
                HomeState(error = result.error.name, isLoading = false)

            is Result.Success -> {
                updateAllEvents(result.data)

            }
        }
    }

    private suspend fun changeMonth(yearMonth: String?) {
        val currentMonthEvents = getEventsGroupedUseCase(
            allEvents[yearMonth], dayFormatter
        )
        _state.update {
            it.copy(
                isLoading = false, error = null,
                currentMonthEvents = currentMonthEvents,
                calendarDays = getCalendarDays(
                    _state.value.currentMonth,
                    currentMonthEvents
                )
            )
        }
    }

    private fun changeSelectedDate(calendarDay: CalendarDay) {
        _state.update {
            it.copy(selectedDate = calendarDay)
        }
    }


    private suspend fun setInitialState() {

        val currentMonth = LocalDateTime.now()

        val currMonthString = getCurrentMonthString(currentMonth)

        val currMonthEvents = allEvents[currMonthString] ?: emptyList()
        val groupedEvents =
            getEventsGroupedUseCase(currMonthEvents, dayFormatter)
        val day = currentMonth.dayOfMonth


        val calendarDays = getCalendarDays(currentMonth, groupedEvents)
        _state.value = HomeState(
            currentMonthEvents = groupedEvents,
            calendarDays = calendarDays,
            yearMonth = currMonthString,
            currentMonth = currentMonth,
            isLoading = false,
            selectedDate = calendarDays.first {
                it.date == day.toString()
            }
        )
    }

    private fun getCurrentMonthString(monthDateTime: LocalDateTime?): String? {
        if (monthDateTime == null) return null
        return monthFormatter.format(monthDateTime)
    }


    private fun updateAllEvents(data: Map<String, List<Event>>) {
        allEvents.putAll(data)
    }

    fun onAction(action: HomeScreenAction) {
        when (action) {
            is HomeScreenAction.ChangeSelectedDate -> changeSelectedDate(action.calendarDay)
            HomeScreenAction.GoToNextMonth -> goToNextMonth()
            HomeScreenAction.GoToPreviousMonth -> goToPreviousMonth()
        }
    }

}