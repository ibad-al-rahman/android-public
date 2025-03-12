package org.ibadalrahman.publicsector.main.view

import android.provider.CalendarContract.Colors
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ibadalrahman.publicsector.navigation.DatePickerModal
import java.text.SimpleDateFormat
import java.util.Date

@Preview
@Composable
fun PrayerTimesContent() {
    var inputDate by remember {
        mutableStateOf(
            SimpleDateFormat("dd/MM/yyyy").format(Date())
        )
    }
    var showDatePicker by remember {
        mutableStateOf(false)
    }

    var prayers = arrayOf(
        Prayer(name = "Fajr", icon = Icons.Default.Nightlight, time = "4:22 AM"),
        Prayer(name = "Sunrise", icon = Icons.Default.WbTwilight, time = "5:52 AM"),
        Prayer(name = "Dhuhr", icon = Icons.Default.WbSunny, time = "11:48 AM"),
        Prayer(name = "Asr", icon = Icons.Default.WbSunny, time = "3:10 PM"),
        Prayer(name = "Maghrib", icon = Icons.Default.WbTwilight, time = "5:48 PM"),
        Prayer(name = "Ishaa", icon = Icons.Default.Nightlight, time = "7:04 PM"),
    )

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F4F4))
            .padding(
                vertical = 30.dp,
                horizontal = 20.dp
            )
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFFFFF))
                .padding(10.dp)
//                .padding(start = 30.dp, top = 10.dp, end = 10.dp, bottom = 10.dp)

        ){
            Text(
                text = "Date",
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            FilledTonalButton(onClick = { showDatePicker = true }) {
                Text(text = inputDate,
                    fontSize = 18.sp)
            }
            if(showDatePicker) {
                DatePickerModal(
                    onDateSelected =  { dateString ->
                        inputDate = dateString
                    },
                    onDismiss = {
                        showDatePicker = false;
                    }
                )
            }
        }
        Spacer(
            modifier = Modifier.height(40.dp)
        )
        Text(
            text = "TIMINGS",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.padding(vertical = 10.dp)
        )
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
           for(prayer in prayers) {
               Row(
                   verticalAlignment = Alignment.CenterVertically,
                   horizontalArrangement = Arrangement.SpaceBetween,
                   modifier = Modifier
                       .fillMaxWidth()
                       .background(Color(0xFFFFFFFF))
                       .padding(20.dp)
               ) {
                   Icon(imageVector = prayer.icon, prayer.name, modifier = Modifier.padding(horizontal = 10.dp))
                   Text(text = prayer.name, fontSize = 18.sp)
                   Spacer(Modifier.weight(1f).fillMaxWidth().background(Color.White)) // height and background only for demonstration
                   Text(prayer.time, fontSize = 18.sp, color = Color.DarkGray, fontWeight = FontWeight.Bold)
               }
           }
        }
        Spacer(
            modifier = Modifier.height(40.dp)
        )
        Text(
            text = "HADITH OF THE WEEK",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.padding(vertical = 10.dp)
        )
        Box(
            modifier = Modifier
                .background(Color(0xFFFFFFFF))
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


class Prayer(val name: String, val icon: ImageVector = Icons.Default.WbSunny, val time : String = "12:00 PM")
