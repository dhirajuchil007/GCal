package com.velocityappsdj.gcal.domain

import com.velocityappsdj.gcal.network.model.response.Event
import com.velocityappsdj.gcal.ui.state.CalendarDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

class GetCalendarDaysUseCase @Inject constructor() {
    suspend operator fun invoke(
        month: LocalDateTime?,
        events: Map<String, List<Event>>
    ): List<CalendarDay> {
        return withContext(Dispatchers.Default) {

            if (month == null)
                return@withContext emptyList()

            val firstDayOfMonth = month.withDayOfMonth(1)
            val yearMonth = YearMonth.from(month)
            val daysInMonth = yearMonth.lengthOfMonth()

            val startDayOfTheWeek = firstDayOfMonth.dayOfWeek.value
            val emptyDaysBefore =
                List(startDayOfTheWeek) { CalendarDay("0", emptyList()) }
            val emptyDaysAfter = List((7 - (startDayOfTheWeek + daysInMonth) % 7) % 7) {
                CalendarDay("0", emptyList())
            }
            var currDay = firstDayOfMonth
            val days = (1..daysInMonth).map { day ->

                val calDay =
                    CalendarDay(day.toString(), events[day.toString()] ?: emptyList(), currDay)
                currDay = currDay.plusDays(1)
                calDay
            }

            val daysOfWeekHeader = listOf(
                CalendarDay("S", emptyList()),
                CalendarDay("M", emptyList()),
                CalendarDay("T", emptyList()),
                CalendarDay("W", emptyList()),
                CalendarDay("T", emptyList()),
                CalendarDay("F", emptyList()),
                CalendarDay("S", emptyList()),
            )


            daysOfWeekHeader + emptyDaysBefore + days + emptyDaysAfter
        }
    }
}