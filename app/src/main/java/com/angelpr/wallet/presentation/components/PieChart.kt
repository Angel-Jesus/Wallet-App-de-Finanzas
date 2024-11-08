package com.angelpr.wallet.presentation.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.angelpr.wallet.presentation.components.model.Categories
import com.angelpr.wallet.presentation.components.model.Type
import com.angelpr.wallet.utils.sumOfFloat

@SuppressLint("DefaultLocale")
@Composable
fun PieChart(
    color: Color = Color.Unspecified,
    data: Map<String, Type>,
    radiusOuter: Dp = 80.dp,
    chartBarWidth: Dp = 35.dp,
    animDuration: Int = 1000,
) {
    val totalSum = data.values.sumOfFloat {
        it.value
    }

    val floatValue = mutableListOf<Float>()

    data.values.forEachIndexed { index, type ->
        floatValue.add(index, 360 * type.value / totalSum)
    }

    var animationPlayed by remember { mutableStateOf(true) }

    var lastValue = 0f

    // it is the diameter value of the Pie
    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) radiusOuter.value * 2f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ),
        label = ""
    )

    // if you want to stabilize the Pie Chart you can use value -90f
    // 90f is used to complete 1/4 of the rotation
    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * 11f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ),
        label = ""
    )

    // to play the animation only once when the function is Created or Recomposed
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Pie Chart using Canvas Arc
        Box(
            modifier = Modifier
                .padding(bottom = 28.dp, top = 16.dp)
                .size(animateSize.dp),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "Deuda Total:\n${data.values.first().typeMoney} ${
                    String.format(
                        "%.2f",
                        totalSum
                    )
                }",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = color
            )

            Canvas(
                modifier = Modifier
                    .offset { IntOffset.Zero }
                    .size(radiusOuter * 2f)
                    .rotate(animateRotation)
            ) {
                // draw each Arc for each data entry in Pie Chart
                data.values.forEachIndexed { index, type ->
                    drawArc(
                        color = type.color,
                        lastValue,
                        floatValue[index],
                        useCenter = false,
                        style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                    )
                    lastValue += floatValue[index]
                }
            }
        }
        // To see the data in more structured way
        // Compose Function in which Items are showing data
        DetailsPieChart(
            color = color,
            data = data
        )

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DetailsPieChart(
    color: Color = Color.Unspecified,
    data: Map<String, Type>
) {
    FlowRow(
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        maxItemsInEachRow = 4
    ) {
        data.forEach { (key, type) ->
            DetailPieChartItem(
                color = color,
                key = key,
                type = type
            )
        }
    }
}

@Composable
private fun DetailPieChartItem(
    color: Color = Color.Unspecified,
    key: String,
    type: Type
) {
    Row(
        modifier = Modifier
            .wrapContentHeight(align = Alignment.CenterVertically)
    ) {
        Canvas(
            modifier = Modifier
                .size(24.dp)
        ) {
            drawCircle(
                color = type.color,
                radius = 5.dp.toPx()
            )
        }
        Text(
            text = key,
            fontSize = 12.sp,
            color = color
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PieChartPreview() {
    MaterialTheme {
        Column {
            PieChart(
                data = mapOf(
                    "Compras" to Categories.Debt[0].copy(value = 30.02f),
                    "Mercado" to Categories.Debt[1].copy(value = 20.02f),
                    "Transporte" to Categories.Debt[2].copy(value = 10.02f),
                    "Transporte2" to Categories.Debt[2].copy(value = 10.02f),
                    "Transporte3" to Categories.Debt[2].copy(value = 10.02f)
                )
            )
        }

    }
}




