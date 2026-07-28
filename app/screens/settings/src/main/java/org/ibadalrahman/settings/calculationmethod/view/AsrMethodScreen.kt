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
import org.ibadalrahman.miqat.Mazhab
import org.ibadalrahman.mvi.BaseScreen
import org.ibadalrahman.resources.R
import org.ibadalrahman.settings.calculationmethod.presenter.CalculationMethodViewModel
import org.ibadalrahman.settings.calculationmethod.presenter.entity.CalculationMethodIntention
import org.ibadalrahman.settings.view.listItemColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsrMethodScreen(
    viewModel: CalculationMethodViewModel,
    onBack: () -> Unit,
) {
    BaseScreen(viewModel = viewModel, viewActionProcessor = { }) { state, intentionProcessor ->
        LoadOnce(intentionProcessor)
        val selected = state.method?.asAstronomical?.mazhab
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.asr_method)) },
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
                item {
                    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))) {
                        MazhabRow(Mazhab.SHAFI, selected, intentionProcessor)
                        Spacer(Modifier.height(1.dp))
                        MazhabRow(Mazhab.HANAFI, selected, intentionProcessor)
                    }
                }
            }
        }
    }
}

@Composable
private fun MazhabRow(
    mazhab: Mazhab,
    selected: Mazhab?,
    intentionProcessor: (CalculationMethodIntention) -> Unit,
) {
    ListItem(
        headlineContent = {
            Text(
                text = stringResource(mazhab.labelRes),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Medium,
                ),
            )
        },
        trailingContent = {
            if (mazhab == selected) {
                Icon(Icons.Filled.Check, contentDescription = null)
            }
        },
        colors = listItemColors,
        modifier = Modifier.clickable {
            intentionProcessor(CalculationMethodIntention.SetMazhab(mazhab))
        },
    )
}
