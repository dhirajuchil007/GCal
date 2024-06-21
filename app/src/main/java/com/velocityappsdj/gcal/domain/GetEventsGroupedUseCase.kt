package com.velocityappsdj.gcal.domain

import com.velocityappsdj.gcal.network.model.response.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetEventsGroupedUseCase @Inject constructor() {

    suspend operator fun invoke(
        events: List<Event>?,
        formatter: DateTimeFormatter
    ): Map<String, List<Event>> {
        return withContext(Dispatchers.Default) {
            val dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

            events?.groupBy {
                val date = LocalDate.parse(it.start.dateTime, dateTimeFormatter)
                formatter.format(date)
            } ?: emptyMap()
        }
    }

}