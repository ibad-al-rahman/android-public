package org.ibadalrahman.adhkar.collection.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import org.ibadalrahman.adhkar.collection.presenter.AdhkarCollectionViewModel
import org.ibadalrahman.adhkar.collection.presenter.entity.AdhkarCollectionIntention
import org.ibadalrahman.adhkar.collection.presenter.entity.AdhkarCollectionViewAction
import org.ibadalrahman.adhkar.domain.entity.AdhkarCollection
import org.ibadalrahman.mvi.BaseScreen
import org.ibadalrahman.resources.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdhkarCollectionScreen(
    viewModel: AdhkarCollectionViewModel,
    openTour: (AdhkarCollection) -> Unit,
) {
    BaseScreen(
        viewModel = viewModel,
        viewActionProcessor = { viewAction ->
            when (viewAction) {
                is AdhkarCollectionViewAction.OpenTour -> openTour(viewAction.collection)
            }
        },
    ) { state, intentionProcessor ->
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(
                        text = stringResource(R.string.adhkar),
                        style = MaterialTheme.typography.displayLarge,
                    )
                })
            },
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.tertiaryContainer),
            ) {
                items(state.collections) { collection ->
                    val interactionSource = remember { MutableInteractionSource() }
                    ListItem(
                        headlineContent = { Text(stringResource(collection.titleRes)) },
                        leadingContent = {
                            Icon(
                                imageVector = collection.icon,
                                contentDescription = stringResource(collection.titleRes),
                            )
                        },
                        trailingContent = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                            )
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.background,
                        ),
                        modifier = Modifier.clickable(
                            interactionSource = interactionSource,
                            indication = null,
                        ) {
                            intentionProcessor(AdhkarCollectionIntention.CollectionTapped(collection))
                        },
                    )
                }
            }
        }
    }
}

private val AdhkarCollection.icon: ImageVector
    get() = when (this) {
        AdhkarCollection.Morning -> Icons.Outlined.WbSunny
        AdhkarCollection.Evening -> Icons.Outlined.DarkMode
    }
