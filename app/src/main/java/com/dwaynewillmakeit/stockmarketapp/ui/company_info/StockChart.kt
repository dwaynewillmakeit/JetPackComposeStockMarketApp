package com.dwaynewillmakeit.stockmarketapp.ui.company_info

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dwaynewillmakeit.stockmarketapp.domain.model.IntradayInfo
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun StockChart(
    infos: List<IntradayInfo> = emptyList(),
    modifier: Modifier = Modifier,
    graphColor: Color = Color.Green
) {

    val spacing = 100f

    val transparentColor = remember {
        graphColor.copy(alpha = 0.5f)
    }

    //Determining the upper bound for the y axis
    val upperValue = remember(infos) {
        (infos.maxOfOrNull { it.close }?.plus(1))?.roundToInt() ?: 0
    }

    //Determining the upper bound for the y axis
    val lowerValue = remember(infos) {
        infos.minOfOrNull { it.close }?.toInt() ?: 0
    }

    //Get the screen density so it can be used scale the text
    val density = LocalDensity.current

    val textPaint = remember {
        Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    Canvas(modifier = modifier) {

        //Calculating the space between each item on the x axis
        val spacePerHour = (size.width - spacing) / infos.size

        //Drawing every 2 hours on thes x axis
        (0 until infos.size - 1 step 2).forEach { i ->
            val info = infos[i]
            val hour = info.date.hour

            drawContext.canvas.nativeCanvas.apply {

                drawText(
                    hour.toString(),
                    spacing + i * spacePerHour,
                    size.height - 5,
                    textPaint
                )
            }
        }

        val priceStep = (upperValue - lowerValue) / 5f

        (0..4).forEach { i ->
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    round(lowerValue + priceStep * i).toString(),
                    30f,
                    size.height - spacing - i * size.height / 5f,
                    textPaint
                )
            }
        }

        var lastX = 0f;

        val strokePath = Path().apply {
            val height = size.height
            for (i in infos.indices) {
                val info = infos[i]
                val nextInfo = infos.getOrNull(i + 1) ?: infos.last()
                val leftRatio =
                    (info.close - lowerValue) / (upperValue - lowerValue) // This is a general formula found on google
                val rightRatio = (nextInfo.close - lowerValue) / (upperValue - lowerValue)

                val x1 = spacing + i * spacePerHour
                val y1 = height - spacing - (leftRatio * height).toFloat()
                val x2 = spacing + (i + 1) * spacePerHour
                val y2 = height - spacing - (rightRatio * height).toFloat()

                //Move to the first point
                if (i == 0) {
                    moveTo(
                        x1,
                        y1
                    )
                }

                lastX = (x1 + x2) / 2f

                quadraticBezierTo(
                    x1, y1, lastX,(x1 + x2) / 2f
                )

            }
        }

        //Copying the path above to reuse as the gradient area of the chart
        val fillPath = android.graphics.Path(strokePath.asAndroidPath()).asComposePath().apply {

            lineTo(lastX, size.height - spacing)
            lineTo(spacing, size.height - spacing)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(transparentColor, Color.Transparent),
                endY = size.height - spacing
            ),

        )

        drawPath(
            path = strokePath,
            color = graphColor,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
    }

}