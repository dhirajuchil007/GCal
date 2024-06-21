package com.velocityappsdj.gcal.network.model.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    @SerializedName("attachments")
    val attachments: List<Attachment?>?,
    @SerializedName("attendees")
    val attendees: List<Attendee?>?,
    @SerializedName("conferenceData")
    val conferenceData: ConferenceData?,
    @SerializedName("created")
    val created: String?,
    @SerializedName("creator")
    val creator: Creator?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("end")
    val end: End,
    @SerializedName("etag")
    val etag: String?,
    @SerializedName("eventType")
    val eventType: String?,
    @SerializedName("guestsCanInviteOthers")
    val guestsCanInviteOthers: Boolean?,
    @SerializedName("guestsCanModify")
    val guestsCanModify: Boolean?,
    @SerializedName("hangoutLink")
    val hangoutLink: String?,
    @SerializedName("htmlLink")
    val htmlLink: String?,
    @SerializedName("iCalUID")
    val iCalUID: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("kind")
    val kind: String?,
    @SerializedName("location")
    val location: String?,
    @SerializedName("organizer")
    val organizer: Organizer?,
    @SerializedName("originalStartTime")
    val originalStartTime: OriginalStartTime?,
    @SerializedName("privateCopy")
    val privateCopy: Boolean?,
    @SerializedName("recurringEventId")
    val recurringEventId: String?,
    @SerializedName("reminders")
    val reminders: Reminders?,
    @SerializedName("sequence")
    val sequence: Int?,
    @SerializedName("start")
    val start: Start,
    @SerializedName("status")
    val status: String?,
    @SerializedName("summary")
    val summary: String?,
    @SerializedName("updated")
    val updated: String?
):Parcelable