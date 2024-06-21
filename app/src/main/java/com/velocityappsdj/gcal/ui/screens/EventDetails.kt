package com.velocityappsdj.gcal.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.velocityappsdj.gcal.network.model.response.Attendee
import com.velocityappsdj.gcal.network.model.response.End
import com.velocityappsdj.gcal.network.model.response.Event
import com.velocityappsdj.gcal.network.model.response.Start
import com.velocityappsdj.gcal.ui.theme.GCalTheme
import java.time.format.DateTimeFormatter

val hourFormatter = DateTimeFormatter.ofPattern("hh:mm a")
val dateFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

@Composable
fun EventDetails(
    modifier: Modifier = Modifier, event: Event?,
    navigateBack: () -> Unit
) {
    if (event != null)
        Scaffold(topBar = {
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = navigateBack) {
                    Icon(Icons.Filled.Close, "Back")
                }
            }
        }) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = event?.summary ?: "No title",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "${getFormattedTime(event.start.dateTime)} - ${getFormattedTime(event.end.dateTime)}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Participants", style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.tertiary
                    )
                )
                LazyColumn {
                    items(event.attendees?.size ?: 0) {
                        Text(
                            modifier = Modifier.padding(vertical = 4.dp),
                            text = event.attendees?.get(
                                it
                            )?.email ?: ""
                            ?: "",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }
    else
        navigateBack()
}

fun getFormattedTime(dateTime: String): String {
    val date = dateFormatter.parse(dateTime)
    return hourFormatter.format(date)
}

@Preview
@Composable
private fun EventDetailsPreview() {
    GCalTheme {
        EventDetails(
            event = Event(
                start = Start("2024-05-21T12:00:00Z", "UTC"),
                summary = "Zeta Interview | Round 2",
                attachments = null,
                attendees = listOf(
                    Attendee(
                        displayName = "John",
                        email = "John@example.com",
                        organizer = false,
                        responseStatus = null,
                        self = false,
                        optional = false
                    ),
                    Attendee(
                        displayName = "Simon",
                        email = "Simon@example.com",
                        organizer = false,
                        responseStatus = null,
                        self = false,
                        optional = false
                    )
                ),
                conferenceData = null,
                created = null,
                creator = null,
                description = null,
                end = End("2024-05-21T13:00:00Z", "UTC"),
                etag = null,
                eventType = null,
                guestsCanInviteOthers = null,
                guestsCanModify = null,
                hangoutLink = null,
                htmlLink = null,
                iCalUID = null,
                id = null,
                kind = null,
                location = null,
                organizer = null,
                originalStartTime = null,
                privateCopy = null,
                recurringEventId = null,
                reminders = null,
                sequence = null,
                status = null,
                updated = null
            )
        ) {}
    }
}