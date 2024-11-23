package org.ibadalrahman.base

import kotlinx.coroutines.CoroutineDispatcher

data class CoroutineDispatchers constructor(
    val io: CoroutineDispatcher,
    val computation: CoroutineDispatcher,
    val main: CoroutineDispatcher,
)
