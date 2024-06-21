package com.velocityappsdj.gcal.ui.actions

import com.velocityappsdj.gcal.ui.state.CalendarDay

sealed class HomeScreenAction {
    data object GoToPreviousMonth : HomeScreenAction()
    data object GoToNextMonth : HomeScreenAction()
    data class ChangeSelectedDate(val calendarDay: CalendarDay) : HomeScreenAction()

}