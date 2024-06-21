package com.velocityappsdj.gcal.domain

import com.velocityappsdj.gcal.common.dummyEvents
import com.velocityappsdj.gcal.repo.EventsRepo
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.extension.ExtendWith
import com.google.common.truth.Truth.assertThat
import com.velocityappsdj.gcal.common.dummyGroupedEvents
import io.mockk.MockKAnnotations
import org.junit.jupiter.api.Test
import java.time.format.DateTimeFormatter


@ExtendWith(MockKExtension::class)
class GetEventsGroupedUseCaseTest {

    @MockK
    lateinit var repository: EventsRepo

    lateinit var sut: GetEventsGroupedUseCase

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        sut = GetEventsGroupedUseCase()
    }


    @Test
    fun `getEventsMap takes a list of events and returns grouped by month`() {
        val monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM")
        val result = sut(dummyEvents, monthFormatter)

        assertThat(result).isEqualTo(dummyGroupedEvents)


    }

    @Test
    fun `getEventsMap returns empty map if list is empty`() {
        val monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM")
        val result = sut(emptyList(), monthFormatter)
        assertThat(result).isEmpty()
    }


}