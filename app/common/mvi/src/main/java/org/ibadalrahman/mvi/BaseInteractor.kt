package org.ibadalrahman.mvi

import kotlinx.coroutines.flow.Flow

interface BaseInteractor<in Action: Any, out Result: Any> {
    suspend fun resultFrom(action: Action): Flow<Result>
}
