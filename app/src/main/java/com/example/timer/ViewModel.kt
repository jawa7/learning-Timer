//package com.example.timer
//
//import android.os.CountDownTimer
//import androidx.compose.runtime.*
//import androidx.lifecycle.ViewModel
//
//class ViewModel: ViewModel() {
//
//    var seconds: Int = 0
//    val started = mutableStateOf(false)
//    val finished = mutableStateOf(true)
//    val progress = mutableStateOf(seconds)
//
//    fun start(seconds: Int) {
//        val countDownTimer = object : CountDownTimer(seconds * 1000L, 10L) {
//            override fun onFinish() {
//            }
//
//            override fun onTick(millisUntilFinished: Long) {
//                progress.value = ((millisUntilFinished / 10L) * 100).toInt()
//            }
//        }
//    }
//}