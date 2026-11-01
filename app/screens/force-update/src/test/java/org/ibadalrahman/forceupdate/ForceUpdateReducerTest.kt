package org.ibadalrahman.forceupdate

import org.ibadalrahman.forceupdate.domain.entity.ForceUpdateResult
import org.ibadalrahman.forceupdate.presenter.ForceUpdateReducer
import org.ibadalrahman.forceupdate.presenter.entity.ForceUpdateScreenState
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ForceUpdateReducerTest {

    @Test
    fun `UpdateRequested marks the state as starting update`() {
        val state = ForceUpdateReducer.reduce(
            prevState = ForceUpdateScreenState.Empty,
            result = ForceUpdateResult.UpdateRequested,
        )

        assertTrue(state.isStartingUpdate)
    }

    @Test
    fun `Empty state is not starting an update`() {
        assertFalse(ForceUpdateScreenState.Empty.isStartingUpdate)
    }
}
