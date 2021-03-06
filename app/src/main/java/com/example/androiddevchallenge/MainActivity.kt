/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    val mainVm = MainViewModel()
    val timerIsStart: Boolean? by mainVm.timerState.observeAsState(null)

    Surface(color = MaterialTheme.colors.background) {
        Column {
            Text(fontSize = 30.sp, text = "FOR TIME", fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            when (timerIsStart) {
                true -> TimerStartPage(mainVm = mainVm)
                false -> TimerSettingPage(mainVm = mainVm)
                null -> EndTimePage(mainVm = mainVm)
            }
        }
    }
}

@Composable
fun EndTimePage(mainVm: MainViewModel) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "End Time", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
            Button(onClick = { mainVm.changeTimerState() }, modifier = Modifier.padding(0.dp, 40.dp, 0.dp, 0.dp)) {
                Text(text = "Reset", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Composable
fun TimerStartPage(mainVm: MainViewModel) {

    val min: Int by mainVm.min.observeAsState(0)
    val sec: Int by mainVm.sec.observeAsState(0)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Left Time", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 20.dp, 0.dp, 0.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = mainVm.getTime(min) + " : " + mainVm.getTime(sec), fontSize = 20.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(4.dp)
                )
            }
            Button(onClick = { mainVm.changeTimerState() }, modifier = Modifier.padding(0.dp, 40.dp, 0.dp, 0.dp)) {
                Text(text = "Stop", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Composable
fun TimerSettingPage(mainVm: MainViewModel) {
    val timeSet: List<Int>? by mainVm.timeSet.observeAsState()
    val selectedTime: Int? by mainVm.selectedTime.observeAsState()
    val timerExpanded: Boolean by mainVm.timerExpanded.observeAsState(false)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "As fast as possible for time", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 20.dp, 0.dp, 0.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Time cap : ", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(4.dp))
                Text(
                    text = "$selectedTime minute", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .border(
                            2.dp,
                            colorResource(id = R.color.black)
                        )
                        .padding(4.dp)
                        .clickable {
                            mainVm.timerExpanded()
                        }
                )
                Box(modifier = Modifier.wrapContentWidth(), contentAlignment = Alignment.Center) {
                    DropdownMenu(expanded = timerExpanded, onDismissRequest = { mainVm.timerExpanded(true) }) {
                        timeSet?.forEach {
                            DropdownMenuItem(onClick = { mainVm.onSelectTime(it) }) {
                                Text(text = "$it minute", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            Button(onClick = { mainVm.changeTimerState() }, modifier = Modifier.padding(0.dp, 40.dp, 0.dp, 0.dp)) {
                Text(text = "Start", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(4.dp))
            }
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}
//
// @Preview("Dark Theme", widthDp = 360, heightDp = 640)
// @Composable
// fun DarkPreview() {
//    MyTheme(darkTheme = true) {
//        MyApp()
//    }
// }
