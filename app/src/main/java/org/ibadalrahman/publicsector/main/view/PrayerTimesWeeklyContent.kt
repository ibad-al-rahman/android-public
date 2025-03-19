package org.ibadalrahman.publicsector.main.view


import Prayer
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ibadalrahman.publicsector.R

@Preview
@Composable
fun PrayerTimesWeeklyContent(
    isLoading: Boolean = false,
    prayers: List<List<String>> = listOf()
) {

    val dayNames = arrayOf(
        stringResource(id = R.string.saturday),
        stringResource(id = R.string.sunday),
        stringResource(id = R.string.monday),
        stringResource(id = R.string.tuesday),
        stringResource(id = R.string.wednesday),
        stringResource(id = R.string.thursday),
        stringResource(id = R.string.friday)
    )

//    val prayerTimes = arrayOf(
//        arrayOf(stringResource(id = R.string.saturday), "4:27 " + stringResource(R.string.am), "5:58 " + stringResource(R.string.am), "11:49 " + stringResource(R.string.am), "3:08 " + stringResource(R.string.pm), "5:44 " + stringResource(R.string.pm), "7:01 " + stringResource(R.string.pm)),
//        arrayOf(stringResource(id = R.string.sunday), "4:27 " + stringResource(R.string.am), "5:58 " + stringResource(R.string.am), "11:49 " + stringResource(R.string.am), "3:08 " + stringResource(R.string.pm), "5:44 " + stringResource(R.string.pm), "7:01 " + stringResource(R.string.pm)),
//        arrayOf(stringResource(id = R.string.monday), "4:27 " + stringResource(R.string.am), "5:58 " + stringResource(R.string.am), "11:49 " + stringResource(R.string.am), "3:08 " + stringResource(R.string.pm), "5:44 " + stringResource(R.string.pm), "7:01 " + stringResource(R.string.pm)),
//        arrayOf(stringResource(id = R.string.tuesday), "4:27 " + stringResource(R.string.am), "5:58 " + stringResource(R.string.am), "11:49 " + stringResource(R.string.am), "3:08 " + stringResource(R.string.pm), "5:44 " + stringResource(R.string.pm), "7:01 " + stringResource(R.string.pm)),
//        arrayOf(stringResource(id = R.string.wednesday), "4:27 " + stringResource(R.string.am), "5:58 " + stringResource(R.string.am), "11:49 " + stringResource(R.string.am), "3:08 " + stringResource(R.string.pm), "5:44 " + stringResource(R.string.pm), "7:01 " + stringResource(R.string.pm)),
//        arrayOf(stringResource(id = R.string.thursday), "4:27 " + stringResource(R.string.am), "5:58 " + stringResource(R.string.am), "11:49 " + stringResource(R.string.am), "3:08 " + stringResource(R.string.pm), "5:44 " + stringResource(R.string.pm), "7:01 " + stringResource(R.string.pm)),
//        arrayOf(stringResource(id = R.string.friday), "4:27 " + stringResource(R.string.am), "5:58 " + stringResource(R.string.am), "11:49 " + stringResource(R.string.am), "3:08 " + stringResource(R.string.pm), "5:44 " + stringResource(R.string.pm), "7:01 " + stringResource(R.string.pm)),
//    )

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(
                vertical = 30.dp,
                horizontal = 20.dp
            )
    ) {
        Text(
            text = stringResource(id = R.string.timings).uppercase(),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(vertical = 10.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(ScrollState(0))
                .background(MaterialTheme.colorScheme.background)
        ) {

            Row(Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.week), fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(8.dp).width(60.dp), textAlign = TextAlign.Center)
                Text(stringResource(id = R.string.fajr), fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(8.dp).width(90.dp), textAlign = TextAlign.Center)
                Text(stringResource(id = R.string.sunrise), fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(8.dp).width(90.dp), textAlign = TextAlign.Center)
                Text(stringResource(id = R.string.dhuhr), fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(8.dp).width(90.dp), textAlign = TextAlign.Center)
                Text(stringResource(id = R.string.asr), fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(8.dp).width(90.dp), textAlign = TextAlign.Center)
                Text(stringResource(id = R.string.maghrib), fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(8.dp).width(90.dp), textAlign = TextAlign.Center)
                Text(stringResource(id = R.string.ishaa), fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(8.dp).width(90.dp), textAlign = TextAlign.Center)
            }

            prayers.forEachIndexed { idx, day ->
                HorizontalDivider( Modifier.width(710.dp))
                Row(
                ) {
                    Text(text = dayNames[idx], fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(8.dp).width(60.dp), textAlign = TextAlign.Center)
                    day.forEachIndexed { index, entry ->
                        Text(entry.replace("am", stringResource(R.string.am)).replace("pm", stringResource(R.string.pm)), fontSize = 18.sp, modifier = Modifier.padding(8.dp).width(90.dp), textAlign = TextAlign.Center)
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier.height(40.dp)
        )
        Text(
            text = stringResource(id = R.string.hadith).uppercase(),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(vertical = 10.dp)
        )
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "انسخ الحديث هنا",
                textAlign = TextAlign.Right,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
