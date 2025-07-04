package org.ibadalrahman.publicsector.main.domain

import org.ibadalrahman.mvi.BaseInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainActivityInteractor @Inject constructor(): BaseInteractor<MainAction, MainResult> {
    override suspend fun resultFrom(action: MainAction): Flow<MainResult> = flow {}
}
