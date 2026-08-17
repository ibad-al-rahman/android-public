package org.ibadalrahman.adhkar.collection.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
    val layoutDirection = LocalLayoutDirection.current
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
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                contentPadding = PaddingValues(20.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = padding.calculateTopPadding(),
                        start = padding.calculateStartPadding(layoutDirection),
                        end = padding.calculateEndPadding(layoutDirection),
                    )
                    .background(MaterialTheme.colorScheme.tertiaryContainer),
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp)),
                    ) {
                        state.collections.forEachIndexed { index, collection ->
                            if (index > 0) Spacer(Modifier.height(1.dp))
                            CollectionRow(
                                collection = collection,
                                onClick = {
                                    intentionProcessor(
                                        AdhkarCollectionIntention.CollectionTapped(collection)
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CollectionRow(
    collection: AdhkarCollection,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(
                text = stringResource(collection.titleRes),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Medium,
                ),
            )
        },
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
        modifier = Modifier.clickable(onClick = onClick),
    )
}

private val AdhkarCollection.icon: ImageVector
    get() = when (this) {
        AdhkarCollection.Morning -> Icons.Outlined.WbSunny
        AdhkarCollection.Evening -> Icons.Outlined.DarkMode
    }
