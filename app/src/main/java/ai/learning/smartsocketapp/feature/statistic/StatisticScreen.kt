package ai.learning.smartsocketapp.feature.statistic

import ai.learning.smartsocketapp.core.model.StatisticData
import android.text.Layout
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.component.fixed
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.insets
import com.patrykandpatrick.vico.compose.common.shader.verticalGradient
import com.patrykandpatrick.vico.compose.common.shape.markerCorneredShape
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.LayeredComponent
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.shader.ShaderProvider
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

private val DayRangeProvider = CartesianLayerRangeProvider.fixed(minY = 200.0, maxY = 250.0)
private val WeekRangeProvider = CartesianLayerRangeProvider.fixed(minY = 200.0, maxY = 250.0)

private val VoltageFormat = DecimalFormat("#.##' V'")
private val VoltageStartAxisFormatter = CartesianValueFormatter.decimal(VoltageFormat)
private val VoltageMarkerFormatter = DefaultCartesianMarker.ValueFormatter.default(VoltageFormat)

private val CurrentFormat = DecimalFormat("#.##' A'")
private val CurrentStartAxisFormatter = CartesianValueFormatter.decimal(CurrentFormat)
private val CurrentMarkerFormatter = DefaultCartesianMarker.ValueFormatter.default(CurrentFormat)

private enum class Timeframe { DAY, WEEK }
private enum class ChartType { VOLTAGE, CURRENT }

@Composable
fun StatisticScreen(
    viewModel: StatisticViewModel
) {
    val dailyStats by viewModel.dailyStats.collectAsState()
    val isDailyLoading by viewModel.isDailyLoading.collectAsState()
    val weeklyStats by viewModel.weeklyStats.collectAsState()
    val isWeeklyLoading by viewModel.isWeeklyLoading.collectAsState()
    val sensorData by viewModel.sensorData.collectAsState()

    var timeframe by remember { mutableStateOf(Timeframe.DAY) }
    var chartType by remember { mutableStateOf(ChartType.VOLTAGE) }

    val voltageColor = Color(0xFF4CAF50)
    val currentColor = Color(0xFF009688)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(Modifier.height(24.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(
                    ChartType.VOLTAGE to "Voltage",
                    ChartType.CURRENT to "Current"
                ).forEach { (type, label) ->
                    val selected = chartType == type
                    val color = if (type == ChartType.VOLTAGE) voltageColor else currentColor

                    Box(
                        Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .height(48.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (selected) color else Color(0xFFF0F4F8))
                            .clickable { chartType = type },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            label,
                            color = if (selected) Color.White else color,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            if (sensorData != null) {
                val voltage = sensorData!!.voltage
                val current = sensorData!!.current
                val instant = sensorData!!.timestamp
                val formattedTime = remember(instant) {
                    java.time.ZonedDateTime.ofInstant(instant, java.time.ZoneId.systemDefault())
                        .format(
                            DateTimeFormatter.ofPattern(
                                "h:mm a, MMM dd, yyyy",
                                Locale.getDefault()
                            )
                        )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                ) {
                    if (chartType == ChartType.VOLTAGE) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                "%.1f V".format(voltage),
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.Black
                            )
                        }
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "%.2f A".format(current),
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.Black
                            )
                        }
                    }
                }
                Text(
                    formattedTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray,
                    modifier = Modifier.align(Alignment.Start)
                )
            } else {
                Text(
                    "Loading...",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Gray
                )
            }

            Spacer(Modifier.height(8.dp))

            StatisticChart(
                timeframe = timeframe,
                dailyStats = dailyStats,
                isDailyLoading = isDailyLoading,
                weeklyStats = weeklyStats,
                isWeeklyLoading = isWeeklyLoading,
                chartType = chartType
            )

            Spacer(Modifier.height(24.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                listOf(Timeframe.DAY to "Last 24 Hours", Timeframe.WEEK to "Last 7 Days")
                    .forEach { (tf, label) ->
                        val selected = tf == timeframe
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (selected) Color.White else Color.Gray,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clip(RoundedCornerShape(50))
                                .background(if (selected) Color(0xFF29B6F6) else Color(0xFFE0E0E0))
                                .clickable { timeframe = tf }
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                        )
                    }
            }
        }
    }
}


@Composable
private fun StatisticChart(
    timeframe: Timeframe,
    dailyStats: List<StatisticData>,
    isDailyLoading: Boolean,
    weeklyStats: List<StatisticData>,
    isWeeklyLoading: Boolean,
    chartType: ChartType
) {
    val isLoading = if (timeframe == Timeframe.DAY) isDailyLoading else isWeeklyLoading
    val points = if (timeframe == Timeframe.DAY) dailyStats else weeklyStats

    val voltageColor = Color(0xFF4CAF50) // Material Green 500
    val currentColor = Color(0xFF009688) // Material Teal 500
    val chartColor = if (chartType == ChartType.VOLTAGE) voltageColor else currentColor

    when {
        isLoading -> {
            Box(
                Modifier
                    .height(240.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        points.isEmpty() -> {
            Box(
                Modifier
                    .height(240.dp)
                    .fillMaxWidth()
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                Text("No data available", color = Color.Gray)
            }
        }

        else -> {
            val x = points.indices.map { it }
            val y = when (chartType) {
                ChartType.VOLTAGE -> points.map { it.avgVoltage }
                ChartType.CURRENT -> points.map { it.avgCurrent }
            }

            val model = remember { CartesianChartModelProducer() }
            val scope = rememberCoroutineScope()

            LaunchedEffect(timeframe, chartType, points) {
                scope.launch {
                    model.runTransaction {
                        lineSeries { series(x, y) }
                    }
                }
            }

            CartesianChartHost(
                rememberCartesianChart(
                    rememberLineCartesianLayer(
                        lineProvider = LineCartesianLayer.LineProvider.series(
                            LineCartesianLayer.rememberLine(
                                fill = LineCartesianLayer.LineFill.single(fill(chartColor)),
                                areaFill = LineCartesianLayer.AreaFill.single(
                                    fill(
                                        ShaderProvider.verticalGradient(
                                            arrayOf(
                                                chartColor.copy(alpha = 0.3f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                                ),
                            )
                        ),
                        rangeProvider = if (chartType == ChartType.VOLTAGE)
                            (if (timeframe == Timeframe.DAY) DayRangeProvider else WeekRangeProvider)
                        else
                            CartesianLayerRangeProvider.fixed(minY = 0.0, maxY = 10.0),
                    ),
                    startAxis = VerticalAxis.rememberStart(
                        valueFormatter = if (chartType == ChartType.VOLTAGE) VoltageStartAxisFormatter else CurrentStartAxisFormatter,
                        guideline = rememberAxisGuidelineComponent(
                            fill = fill(Color.Gray.copy(alpha = 0.5f)),
                            thickness = 1.dp
                        )
                    ),
                    bottomAxis = HorizontalAxis.rememberBottom(
                        valueFormatter = CartesianValueFormatter { _, value, _ ->
                            val idx = value.toInt().coerceIn(0, points.lastIndex)
                            if (timeframe == Timeframe.DAY) {
                                val hour =
                                    points[idx].timestamp.atZone(java.time.ZoneId.systemDefault()).hour
                                "$hour h"
                            } else {
                                points[idx].timestamp.atZone(java.time.ZoneId.systemDefault()).dayOfWeek
                                    .getDisplayName(TextStyle.SHORT, Locale.getDefault())
                            }
                        },
                        guideline = rememberAxisGuidelineComponent(
                            fill = fill(Color.Gray.copy(alpha = 0.5f)),
                            thickness = 1.dp
                        )
                    ),
                    marker = rememberMarker(
                        if (chartType == ChartType.VOLTAGE) VoltageMarkerFormatter else CurrentMarkerFormatter,
                    )
                ),
                modelProducer = model,
                modifier = Modifier
                    .height(240.dp)
                    .fillMaxWidth(),
                scrollState = rememberVicoScrollState(scrollEnabled = false)
            )
        }
    }
}


@Composable
private fun rememberMarker(
    valueFormatter: DefaultCartesianMarker.ValueFormatter,
    showIndicator: Boolean = true
): CartesianMarker {
    val labelBgShape = markerCorneredShape(CorneredShape.Corner.Rounded)
    val bg = rememberShapeComponent(
        fill = fill(MaterialTheme.colorScheme.background),
        shape = labelBgShape,
        strokeThickness = 1.dp,
        strokeFill = fill(MaterialTheme.colorScheme.outline)
    )
    val label = rememberTextComponent(
        color = MaterialTheme.colorScheme.onSurface,
        textAlignment = Layout.Alignment.ALIGN_CENTER,
        padding = insets(8.dp, 4.dp),
        background = bg,
        minWidth = TextComponent.MinWidth.fixed(40.dp)
    )
    val indicatorFront = rememberShapeComponent(
        fill = fill(MaterialTheme.colorScheme.surface),
        shape = CorneredShape.Pill
    )
    val guideline = rememberAxisGuidelineComponent()

    return rememberDefaultCartesianMarker(
        label = label,
        valueFormatter = valueFormatter,
        indicator = if (showIndicator) {
            { color ->
                LayeredComponent(
                    back = ShapeComponent(
                        fill = fill(color.copy(alpha = 0.15f)),
                        shape = CorneredShape.Pill
                    ),
                    front = LayeredComponent(
                        back = ShapeComponent(fill = fill(color), shape = CorneredShape.Pill),
                        front = indicatorFront,
                        padding = insets(5.dp)
                    ),
                    padding = insets(10.dp)
                )
            }
        } else null,
        indicatorSize = 36.dp,
        guideline = guideline
    )
}
