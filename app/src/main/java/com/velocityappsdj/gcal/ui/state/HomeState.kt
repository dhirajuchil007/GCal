package com.velocityappsdj.gcal.ui.state

import com.velocityappsdj.gcal.network.model.response.Event
import java.time.LocalDateTime

data class CalendarDay(
    val date: String,
    val events: List<Event>,
    val localDateTime: LocalDateTime? = null
)

data class HomeState(
    val isLoading: Boolean, private val error: String? = null,
    val currentMonthEvents: Map<String, List<Event>>? = null,
    val yearMonth: String? = null,
    val currentMonth: LocalDateTime? = null,
    val selectedDate: CalendarDay? = null,
    val calendarDays: List<CalendarDay>? = null
)