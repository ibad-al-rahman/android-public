package org.ibadalrahman.adhkar.tour.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import org.ibadalrahman.adhkar.tour.presenter.AdhkarTourViewModel
import org.ibadalrahman.adhkar.tour.presenter.entity.AdhkarTourIntention
import org.ibadalrahman.adhkar.tour.presenter.entity.AdhkarTourScreenState
import org.ibadalrahman.adhkar.tour.presenter.entity.AdhkarTourViewAction
import org.ibadalrahman.mvi.BaseScreen
import org.ibadalrahman.mvi.ObserveLifecycleEvents
import org.ibadalrahman.resources.R

/** Swipe distance (dp) past which a horizontal drag counts as next/previous. Mirrors iOS's 60pt. */
private const val SWIPE_THRESHOLD_DP = 60f

@Composable
fun AdhkarTourScreen(
    viewModel: AdhkarTourViewModel,
    onClose: () -> Unit,
) {
    BaseScreen(
        viewModel = viewModel,
        viewActionProcessor = { viewAction ->
            when (viewAction) {
                AdhkarTourViewAction.Close -> onClose()
            }
        },
    ) { state, intentionProcessor ->
        ObserveLifecycleEvents(
            onStart = { intentionProcessor(AdhkarTourIntention.Load) },
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Header(state = state)
            Content(
                state = state,
                intentionProcessor = intentionProcessor,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun Header(state: AdhkarTourScreenState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            state.position?.let { position ->
                Text(
                    text = "$position / ${state.total}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            state.collection?.let { collection ->
                Text(
                    text = stringResource(collection.titleRes),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
        TourProgressBar(progress = state.progress)
    }
}

@Composable
private fun Content(
    state: AdhkarTourScreenState,
    intentionProcessor: (AdhkarTourIntention) -> Unit,
    modifier: Modifier = Modifier,
) {
    val layoutDirection = LocalLayoutDirection.current
    val thresholdPx = with(LocalDensity.current) { SWIPE_THRESHOLD_DP.dp.toPx() }
    val interactionSource = remember { MutableInteractionSource() }

    val activeDhikr = state.activeDhikr
    if (activeDhikr != null) {
        DhikrContent(
            state = state,
            modifier = modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                ) { intentionProcessor(AdhkarTourIntention.Tapped) }
                .pointerInput(state.activeIndex, layoutDirection) {
                    var totalDrag = 0f
                    detectHorizontalDragGestures(
                        onDragStart = { totalDrag = 0f },
                        onDragEnd = {
                            if (kotlin.math.abs(totalDrag) < thresholdPx) return@detectHorizontalDragGestures
                            // In RTL, dragging toward the reading direction (rightwards, positive)
                            // advances the tour; mirror it for LTR.
                            val forward = if (layoutDirection == LayoutDirection.Rtl) {
                                totalDrag > 0
                            } else {
                                totalDrag < 0
                            }
                            intentionProcessor(
                                if (forward) AdhkarTourIntention.Next else AdhkarTourIntention.Previous,
                            )
                        },
                        onHorizontalDrag = { _, dragAmount -> totalDrag += dragAmount },
                    )
                },
        )
    } else if (state.isFinished) {
        CompletionScreen(
            state = state,
            onFinish = { intentionProcessor(AdhkarTourIntention.Finish) },
            modifier = modifier,
        )
    }
}

@Composable
private fun DhikrContent(
    state: AdhkarTourScreenState,
    modifier: Modifier = Modifier,
) {
    val dhikr = state.activeDhikr ?: return
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // The verse fills the flexible space above the count and centers itself within it, so the
        // count/done block below is pushed to the bottom of the screen.
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = dhikr.displayText(),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Serif,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 8.dp),
            )
        }

        if (state.isActiveComplete) {
            DoneBlock()
        } else {
            CountBlock(count = state.activeCount, target = dhikr.target)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun CountBlock(count: Int, target: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = "$count",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = "/ $target",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(R.string.dhikr_count_hint),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Composable
private fun DoneBlock() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CompletionMark(size = 64.dp)
        Text(
            text = stringResource(R.string.dhikr_done),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(R.string.dhikr_next_hint),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun CompletionScreen(
    state: AdhkarTourScreenState,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val collectionName = state.collection?.let { stringResource(it.titleRes) }.orEmpty()
    Column(
        modifier = modifier.clickable(onClick = onFinish),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CompletionMark(size = 120.dp)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.adhkar_completed, collectionName),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
