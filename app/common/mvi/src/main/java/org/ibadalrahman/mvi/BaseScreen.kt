package org.ibadalrahman.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

@Composable
fun <UiState, Intention, ViewAction, Action, Result, ViewModel: BaseViewModel<UiState, Intention, ViewAction, Action, Result>>
BaseScreen(
    viewModel: ViewModel,
    viewActionProcessor: (viewAction: ViewAction) -> Unit,
    content: @Composable (state: UiState, intentionProcessor: (intention: Intention) -> Unit) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.viewActions.collect { viewActionProcessor(it) }
    }

    content(viewModel.state.collectAsState().value, viewModel.submitIntention)
}
