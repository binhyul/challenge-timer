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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _timeSet: MutableLiveData<List<Int>> = MutableLiveData(listOf())
    val timeSet: LiveData<List<Int>> = _timeSet

    private val _selectedTime: MutableLiveData<Int> = MutableLiveData(1)
    val selectedTime: LiveData<Int> = _selectedTime

    private val _timerExpanded: MutableLiveData<Boolean> = MutableLiveData(false)
    val timerExpanded: LiveData<Boolean> = _timerExpanded

    private val _timerState: MutableLiveData<Boolean?> = MutableLiveData(false)
    val timerState: LiveData<Boolean?> = _timerState

    private val _time: MutableLiveData<Int> = MutableLiveData(0)
    val min: LiveData<Int > = Transformations.map(_time) {
        it / 60
    }

    val sec: LiveData<Int> = Transformations.map(_time) {
        it % 60
    }

    private lateinit var timerJob: Job
    init {
        _timeSet.value = ArrayList<Int>().apply {
            for (i: Int in 1..100) {
                add(i)
            }
        }
    }

    fun onSelectTime(select: Int) {
        _selectedTime.value = select
        _timerExpanded.value = false
    }

    fun timerExpanded(disMiss: Boolean = false) {
        val expanded = timerExpanded.value ?: false
        _timerExpanded.value = if (disMiss) {
            false
        } else {
            !expanded
        }
    }

    fun changeTimerState() {
        val timerState = timerState.value ?: true
        _timerState.value = !timerState

        if (_timerState.value == true) {
            startTimer()
        } else {
            stopTimer()
        }
    }

    private fun startTimer() {
        val selectTime = selectedTime.value ?: 0
        _time.value = selectTime * 60
        timerJob = viewModelScope.launch {
            val time = _time.value ?: return@launch
            repeat(time + 1) {
                _time.value = time - it
                delay(1000)
            }
        }.apply {
            invokeOnCompletion {
                if (_timerState.value == true) {
                    _timerState.value = null
                }
            }
        }

        timerJob.start()
    }

    private fun stopTimer() {
        timerJob.cancel()
    }

    fun getTime(time: Int) = if (time <10) {
        "0$time"
    } else {
        "$time"
    }
}
