package com.example.timer

import android.os.CountDownTimer
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {

    var seconds = mutableStateOf(10)
    var started = mutableStateOf(false)
    var finished = mutableStateOf(true)
    var progress = mutableStateOf(seconds.value * 100)

    private var countDownTimer: CountDownTimer? = null

    fun startCountDown() {
        countDownTimer = object : CountDownTimer(seconds.value * 1000L, 10L) {

            override fun onFinish() {
                finished.value = true
                started.value = false
            }

            override fun onTick(millisUntilFinished: Long) {
                progress.value = (millisUntilFinished / 10L).toInt()
            }
        }
        countDownTimer?.start()
    }

    fun cancelCountDown() {
        countDownTimer?.cancel()
    }
}