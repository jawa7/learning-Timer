package com.example.timer

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@ExperimentalPagerApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                color = Color(0xFF101010),
                modifier = Modifier.fillMaxSize()
            ) {
                var seconds = remember { mutableStateOf(5) }
                Column {
                    Timer(
                        seconds = seconds.value,
                        inactiveBarColor = Color.DarkGray,
                        activeBarColor = Color(0xFF37B900),
                    )

                    val pagerState = rememberPagerState(12)
                    HorizontalPager(state = pagerState, Modifier.size(300.dp)) { page ->
                        Text(
                            text = "Page: ${page}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { seconds.value = page }
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
fun Timer(
    inactiveBarColor: Color,
    activeBarColor: Color,
    seconds: Int
) {
    val started = remember { mutableStateOf(false) }
    val finished = remember { mutableStateOf(true) }
    val progress = remember { mutableStateOf(seconds * 100) }

    val countDownTimer = object : CountDownTimer(seconds * 1000L, 10L) {
        override fun onFinish() {
            finished.value = true
            started.value = false
        }

        override fun onTick(millisUntilFinished: Long) {
            progress.value = (millisUntilFinished / 10L).toInt()
        }
    }

    fun cancel() {
        countDownTimer.cancel()
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(250.dp)
    ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                drawArc(
                    color = inactiveBarColor,
                    startAngle = -90F,
                    sweepAngle = 360F,
                    useCenter = false,
                    style = Stroke(width = 10F)
                )
                val ratio = progress.value.toFloat() / seconds / 100
                val sweepAngle = 360F * (ratio - 1)
                drawArc(
                    color = activeBarColor,
                    startAngle = -90F,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = 10F)
                )
                val radius = size.width / 2.0F
                val x = radius + radius * sin(-PI - 2 * PI * ratio)
                val y = radius + radius * cos(-PI - 2 * PI * ratio)
                drawCircle(
                    color = inactiveBarColor,
                    radius = 20F,
                    center = Offset(x.toFloat(), y.toFloat())
                )
            }
                Text(
                    text = (progress.value / 100).toString(),
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Box {
                    Button(
                        onClick = {
                            if (!started.value) {
                                started.value = true
                                finished.value = false
                                countDownTimer.start()
                            } else {
                                cancel()
                            }
                        },
                        modifier = Modifier.align(Alignment.BottomCenter),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (!started.value) {
                                Color.Green
                            } else {
                                Color.Red
                            }
                        )
                    ) {
                        Text(
                            text = if (!started.value) {
                                "Start"
                            } else {
                                "Stop"
                            }
                        )
                    }
                }
            }
        }


