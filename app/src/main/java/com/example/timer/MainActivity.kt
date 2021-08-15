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
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.PI
import kotlin.math.absoluteValue
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
                val viewModel: TimerViewModel = viewModel()
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Timer(
                        inactiveBarColor = Color.DarkGray,
                        activeBarColor = Color(0xFF37B900),
                    )

                    val pagerState = rememberPagerState(
                        pageCount = 12,
                    )
                    LaunchedEffect(pagerState.currentPage) {
                        viewModel.seconds.value = pagerState.currentPage + 1
                        viewModel.progress.value = pagerState.currentPage + 1
                    }
                    HorizontalPager(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        state = pagerState,
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth(),
                        itemSpacing = 100.dp,
                    ) { page ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .graphicsLayer {
                                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                                    lerp(
                                        start = ScaleFactor(0.55f, 0.55f),
                                        stop = ScaleFactor(1f, 1f),
                                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                    ).also { scale ->
                                        scaleX = scale.scaleX
                                        scaleY = scale.scaleY
                                    }

                                    alpha = lerp(
                                        start = ScaleFactor(0.5f, 0.5f),
                                        stop = ScaleFactor(1f, 1f),
                                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                    ).scaleX
                                },
                        ) {
                            Text(
                                text = "${page + 1}",
                                modifier = Modifier
                                    .clickable(
                                        onClick = {
                                        viewModel.seconds.value = page + 1

                                    }),

                                style = MaterialTheme.typography.h3,
                                color = Color.White
                            )
                        }
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
) {
    val viewModel: TimerViewModel = viewModel()
    Column() {
        Box(
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
                val ratio = viewModel.progress.value.toFloat() / viewModel.seconds.value / 100
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
                text = (viewModel.progress.value / 100).toString(),
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Box(
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Button(
                onClick = {
                    if (!viewModel.started.value) {
                        viewModel.started.value = true
                        viewModel.finished.value = false
                        viewModel.startCountDown()
                    } else {
                        viewModel.cancelCountDown()
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (!viewModel.started.value) {
                        Color.Green
                    } else {
                        Color.Red
                    }
                )
            ) {
                Text(
                    text = if (!viewModel.started.value) {
                        "Start"
                    } else {
                        "Stop"
                    }
                )
            }
        }
    }
}
