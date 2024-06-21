package com.velocityappsdj.gcal.domain


import com.velocityappsdj.gcal.common.NetworkError
import com.velocityappsdj.gcal.common.Result
import com.velocityappsdj.gcal.network.model.response.Event
import com.velocityappsdj.gcal.repo.EventsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


class GetEventsForMonthUseCase @Inject constructor(
    private val eventsRepo: EventsRepo,
    private val getEventsGroupedUseCase: GetEventsGroupedUseCase
) {

    companion object {
        private const val TAG = "GetEventsUseCase"
    }

    suspend operator fun invoke(
        month: LocalDateTime? = null
    ): Result<Map<String, List<Event>>, NetworkError> {
        return withContext(context = Dispatchers.IO) {


            //Fetch only events from current month if monthString is null
            val day = month?.let {
                month.atZone(ZoneId.systemDefault())
            } ?: ZonedDateTime.now()
            val firstDay = day.withDayOfMonth(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            val yearMonth = YearMonth.from(day)
            val lastDayOfMonth =
                yearMonth.atEndOfMonth().atTime(LocalTime.MAX).atZone(ZoneId.systemDefault())
                    .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

            when (val result = eventsRepo.getEvents(timeMin = firstDay, timeMax = lastDayOfMonth)) {
                is Result.Success -> {
                    val monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM")
                    val groupedEvents = getEventsGroupedUseCase(result.data.events, monthFormatter)
                    val map = groupedEvents.toMutableMap()
                    if (groupedEvents.isEmpty()) {
                        map.put(monthFormatter.format(month), emptyList())
                    }

                    Result.Success(map)
                }

                is Result.Error -> {
                    Result.Error(result.error)
                }
            }
        }


    }
}
