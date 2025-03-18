package org.ibadalrahman.publicsector.main.view

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import org.ibadalrahman.publicsector.R
import org.ibadalrahman.publicsector.main.presenter.MainActivityViewModel
import org.ibadalrahman.publicsector.main.presenter.MainIntention
import org.ibadalrahman.publicsector.navigation.DatePickerModal
import org.ibadalrahman.publicsector.navigation.Screen
import org.ibadalrahman.publicsector.navigation.TabBarItem
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PrayerTimesContent(viewModel: MainActivityViewModel) {

    val viewState by viewModel.viewState.collectAsState()

    val tabData = arrayOf(
        stringResource(id=R.string.daily), stringResource(id=R.string.weekly)
    )

    var currentPage = viewState.currentPrayerPage.ordinal

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        TabRow (
            selectedTabIndex = currentPage,
            divider = {
                Spacer(modifier = Modifier.height(5.dp))
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .zIndex(2f)
        ) {
            tabData.forEachIndexed { index, name ->
                Tab(
                    selected = currentPage == index,
                    onClick = {
                        viewModel.handleIntent(
                            if(index == 0) MainIntention.LoadDayView else MainIntention.LoadWeekView
                        )
                    },
                    text = {
                        Text(text = name, fontSize = 18.sp)
                    }
                )
            }
        }

        if(currentPage == 0) {
//        daily
            PrayerTimesDailyContent(
                inputDate = viewState.inputDate,
                isLoading = viewState.isLoading,
                prayers = viewState.prayersDay,
                onDateSelected = { date ->
                    viewModel.handleIntent(MainIntention.SetDate(inputDate = date))
                }
            )
        }
        else {
//        weekly
            PrayerTimesWeeklyContent(
                isLoading = viewState.isLoading,
                prayers = viewState.prayersWeek,
            )
        }
    }

}
