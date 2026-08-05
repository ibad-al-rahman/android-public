package org.ibadalrahman.settings.calculationmethod

import org.ibadalrahman.miqat.repository.data.domain.MiqatCalculationMethod
import org.ibadalrahman.settings.calculationmethod.domain.entity.CalculationMethodResult
import org.ibadalrahman.settings.calculationmethod.presenter.CalculationMethodReducer
import org.ibadalrahman.settings.calculationmethod.presenter.entity.CalculationMethodScreenState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CalculationMethodReducerTest {

    @Test
    fun `Loaded copies both method and preview into state`() {
        val method = MiqatCalculationMethod.default

        val state = CalculationMethodReducer.reduce(
            prevState = CalculationMethodScreenState.Empty,
            result = CalculationMethodResult.Loaded(method = method, preview = null),
        )

        assertEquals(method, state.method)
        assertNull(state.preview)
    }

    @Test
    fun `RequiresAstronomicalSetup leaves state untouched`() {
        val prevState = CalculationMethodScreenState(method = MiqatCalculationMethod.default)

        val state = CalculationMethodReducer.reduce(
            prevState = prevState,
            result = CalculationMethodResult.RequiresAstronomicalSetup,
        )

        assertEquals(prevState, state)
    }
}
