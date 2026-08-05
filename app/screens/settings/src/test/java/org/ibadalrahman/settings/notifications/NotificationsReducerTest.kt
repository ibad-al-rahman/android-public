package org.ibadalrahman.settings.notifications

import org.ibadalrahman.settings.notifications.domain.entity.NotificationsResult
import org.ibadalrahman.settings.notifications.presenter.NotificationsReducer
import org.ibadalrahman.settings.notifications.presenter.entity.NotificationsScreenState
import org.ibadalrahman.settings.repository.data.domain.NotificationSettings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotificationsReducerTest {

    @Test
    fun `Loaded maps every settings field into state`() {
        val settings = NotificationSettings(
            enabled = true,
            fajr = true,
            dhuhr = false,
            asr = true,
            maghrib = false,
            ishaa = true,
            morningAdhkarEnabled = true,
            eveningAdhkarEnabled = false,
            morningHour = 5,
            morningMinute = 30,
            eveningHour = 19,
            eveningMinute = 45,
        )

        val state = NotificationsReducer.reduce(
            prevState = NotificationsScreenState.Default,
            result = NotificationsResult.Loaded(settings),
        )

        assertTrue(state.notificationsEnabled)
        assertTrue(state.fajr)
        assertFalse(state.dhuhr)
        assertTrue(state.asr)
        assertFalse(state.maghrib)
        assertTrue(state.ishaa)
        assertTrue(state.morningAdhkarEnabled)
        assertFalse(state.eveningAdhkarEnabled)
        assertEquals(5, state.morningHour)
        assertEquals(30, state.morningMinute)
        assertEquals(19, state.eveningHour)
        assertEquals(45, state.eveningMinute)
    }

    @Test
    fun `Loaded with defaults produces the Default state`() {
        val state = NotificationsReducer.reduce(
            prevState = NotificationsScreenState.Default.copy(notificationsEnabled = true),
            result = NotificationsResult.Loaded(NotificationSettings.Default),
        )

        assertEquals(NotificationsScreenState.Default, state)
    }
}
