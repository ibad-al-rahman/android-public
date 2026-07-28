package org.ibadalrahman.settings.calculationmethod.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.ibadalrahman.miqat.Coordinates
import org.ibadalrahman.mvi.BaseScreen
import org.ibadalrahman.resources.R
import org.ibadalrahman.settings.calculationmethod.presenter.CalculationMethodViewModel
import org.ibadalrahman.settings.calculationmethod.presenter.entity.CalculationMethodIntention
import org.ibadalrahman.settings.view.listItemColors

/**
 * A city with fixed coordinates.
 *
 * TODO: replace this stub list with a real geocoder (Android [android.location.Geocoder] or a
 *  places API) so arbitrary locations resolve. For now, a small curated list makes the whole
 *  astronomical flow demonstrable end-to-end.
 */
private data class LocationSuggestion(
    val name: String,
    val latitude: Double,
    val longitude: Double,
)

private val stubLocations = listOf(
    LocationSuggestion("Beirut, Lebanon", 33.8938, 35.5018),
    LocationSuggestion("Mecca, Saudi Arabia", 21.4225, 39.8262),
    LocationSuggestion("Cairo, Egypt", 30.0444, 31.2357),
    LocationSuggestion("Istanbul, Türkiye", 41.0082, 28.9784),
    LocationSuggestion("London, United Kingdom", 51.5074, -0.1278),
    LocationSuggestion("New York, United States", 40.7128, -74.0060),
    LocationSuggestion("Kuala Lumpur, Malaysia", 3.1390, 101.6869),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSearchScreen(
    viewModel: CalculationMethodViewModel,
    onLocationSelected: () -> Unit,
    onBack: () -> Unit,
) {
    BaseScreen(viewModel = viewModel, viewActionProcessor = { }) { _, intentionProcessor ->
        LoadOnce(intentionProcessor)
        var query by remember { mutableStateOf("") }
        val results = remember(query) {
            if (query.isBlank()) emptyList()
            else stubLocations.filter { it.name.contains(query, ignoreCase = true) }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.location)) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.go),
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding())
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(20.dp)
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text(stringResource(R.string.search_for_location)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(Modifier.height(16.dp))

                if (query.isBlank()) {
                    EmptyHint()
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        items(results) { suggestion ->
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = suggestion.name,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Medium,
                                        ),
                                    )
                                },
                                colors = listItemColors,
                                modifier = Modifier.clickable {
                                    intentionProcessor(
                                        CalculationMethodIntention.SetCoordinates(
                                            Coordinates(suggestion.latitude, suggestion.longitude)
                                        )
                                    )
                                    onLocationSelected()
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
private fun EmptyHint() {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.search_for_location_description),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.padding(24.dp),
        )
    }
}
