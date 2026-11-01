package org.ibadalrahman.forceupdate.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.ibadalrahman.forceupdate.presenter.ForceUpdateViewModel
import org.ibadalrahman.forceupdate.presenter.entity.ForceUpdateIntention
import org.ibadalrahman.forceupdate.presenter.entity.ForceUpdateViewAction
import org.ibadalrahman.mvi.BaseScreen
import org.ibadalrahman.resources.R

/**
 * Full-screen, non-dismissible update gate. Mirrors the iOS force-update screen: logo, message,
 * and a single "Update Now" button. [onRequestUpdate] is raised to the host Activity, which owns
 * the Play In-App Update flow. [BackHandler] swallows the back button so the user cannot escape it.
 */
@Composable
fun ForceUpdateScreen(
    viewModel: ForceUpdateViewModel,
    onRequestUpdate: () -> Unit,
) {
    BackHandler(enabled = true) { /* blocking: back must not dismiss the update gate */ }

    BaseScreen(
        viewModel = viewModel,
        viewActionProcessor = { viewAction ->
            when (viewAction) {
                ForceUpdateViewAction.LaunchUpdate -> onRequestUpdate()
            }
        },
    ) { state, intentionProcessor ->
        LaunchedEffect(Unit) { intentionProcessor(ForceUpdateIntention.OnAppear) }
        Scaffold { padding ->
            Column(
                verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 32.dp),
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp),
                )

                Text(
                    text = stringResource(R.string.update_available_message),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                )

                Button(
                    onClick = { intentionProcessor(ForceUpdateIntention.OnTapUpdate) },
                    enabled = !state.isStartingUpdate,
                ) {
                    Text(stringResource(R.string.update_now))
                }
            }
        }
    }
}
