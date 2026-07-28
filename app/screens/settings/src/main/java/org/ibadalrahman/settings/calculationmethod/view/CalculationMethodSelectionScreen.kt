package org.ibadalrahman.settings.calculationmethod.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.ibadalrahman.miqat.repository.data.domain.AstronomicalMethod
import org.ibadalrahman.mvi.BaseScreen
import org.ibadalrahman.resources.R
import org.ibadalrahman.settings.calculationmethod.presenter.CalculationMethodViewModel
import org.ibadalrahman.settings.calculationmethod.presenter.entity.CalculationMethodIntention
import org.ibadalrahman.settings.view.NavigationRow
import org.ibadalrahman.settings.view.SectionHeader
import org.ibadalrahman.settings.view.listItemColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculationMethodSelectionScreen(
    viewModel: CalculationMethodViewModel,
    onBack: () -> Unit,
    openLocationSearch: () -> Unit,
) {
    BaseScreen(viewModel = viewModel, viewActionProcessor = { }) { state, intentionProcessor ->
        LoadOnce(intentionProcessor)
        val config = state.method?.asAstronomical
        val currentMethod = config?.method
        val hasLocation = config != null
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.astronomical_method)) },
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
            LazyColumn(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                contentPadding = PaddingValues(20.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = padding.calculateTopPadding())
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                // Location
                item {
                    val locationBadge = config?.coordinates?.let {
                        "%.2f, %.2f".format(it.latitude, it.longitude)
                    }
                    NavigationRow(
                        text = stringResource(R.string.location),
                        icon = Icons.Filled.LocationOn,
                        onClick = openLocationSearch,
                        badge = locationBadge,
                        modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                    )
                    if (!hasLocation) {
                        Text(
                            text = stringResource(R.string.select_location_first),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                        )
                    }
                }

                item { Spacer(Modifier.height(24.dp)) }

                // Preset methods
                item {
                    SectionHeader(stringResource(R.string.astronomical_method))
                    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))) {
                        presetMethods.forEachIndexed { index, method ->
                            if (index > 0) Spacer(Modifier.height(1.dp))
                            val selected = (currentMethod as? AstronomicalMethod.Preset)
                                ?.method == method
                            MethodRow(
                                label = stringResource(method.labelRes),
                                selected = selected,
                                enabled = hasLocation,
                                onClick = {
                                    intentionProcessor(
                                        CalculationMethodIntention.SetMethod(
                                            AstronomicalMethod.Preset(method)
                                        )
                                    )
                                },
                            )
                        }
                        Spacer(Modifier.height(1.dp))
                        MethodRow(
                            label = stringResource(R.string.method_custom),
                            selected = currentMethod is AstronomicalMethod.Custom,
                            enabled = hasLocation,
                            onClick = {
                                // Seed sensible defaults (18°/18°) when switching to custom.
                                val existing = currentMethod as? AstronomicalMethod.Custom
                                intentionProcessor(
                                    CalculationMethodIntention.SetMethod(
                                        AstronomicalMethod.Custom(
                                            fajrAngle = existing?.fajrAngle ?: 18.0,
                                            ishaaAngle = existing?.ishaaAngle ?: 18.0,
                                        )
                                    )
                                )
                            },
                        )
                    }
                }

                // Custom angle editors (only when custom is selected)
                if (currentMethod is AstronomicalMethod.Custom) {
                    item {
                        Spacer(Modifier.height(24.dp))
                        SectionHeader(stringResource(R.string.custom_provider))
                        Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))) {
                            AngleRow(
                                label = stringResource(R.string.custom_fajr_angle),
                                angle = currentMethod.fajrAngle,
                                enabled = hasLocation,
                                onAngleChange = {
                                    intentionProcessor(
                                        CalculationMethodIntention.SetMethod(
                                            currentMethod.copy(fajrAngle = it)
                                        )
                                    )
                                },
                            )
                            Spacer(Modifier.height(1.dp))
                            AngleRow(
                                label = stringResource(R.string.custom_ishaa_angle),
                                angle = currentMethod.ishaaAngle,
                                enabled = hasLocation,
                                onAngleChange = {
                                    intentionProcessor(
                                        CalculationMethodIntention.SetMethod(
                                            currentMethod.copy(ishaaAngle = it)
                                        )
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
private fun MethodRow(
    label: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Medium,
                ),
            )
        },
        trailingContent = {
            if (selected) Icon(Icons.Filled.Check, contentDescription = null)
        },
        colors = listItemColors,
        modifier = Modifier.clickable(enabled = enabled, onClick = onClick),
    )
}
