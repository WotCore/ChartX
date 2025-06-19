package wot.core.view.chartx.axis

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathEffect
import android.graphics.RectF
import wot.core.view.chartx.axis.formatter.AxisLabelFormatter
import wot.core.view.chartx.axis.model.AxisLabel
import wot.core.view.chartx.axis.model.AxisLine
import wot.core.view.chartx.axis.model.AxisPosition
import wot.core.view.chartx.axis.model.AxisSide
import wot.core.view.chartx.ext.centerY
import wot.core.view.chartx.ext.textWidth

/**
 * 图表坐标轴
 * 包含 [AxisLine] [AxisLabel]
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
class ChartAxis(
    val axisSide: AxisSide,
    val axisPosition: AxisPosition,
    val axisSize: Float,
) {
    var labelTextSize: Float = 30F
    var labelTextColor: Int = Color.parseColor("#333333")

    var formatter: AxisLabelFormatter? = null

    /**
     * 标签与轴之间的间距, 防止贴边。
     * [AxisSide.LEFT] 和 [AxisSide.RIGHT] 使用到。
     */
    var horizontalMargin = 8F

    /**
     * 画板内容的绘制边界
     */
    private var panelContentBounds: RectF? = null

    /**
     * 坐标轴的绘制边界
     */
    private val axisBounds by lazy { RectF() }

    private val paint by lazy { Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL } }

    private val labelList by lazy { mutableListOf<AxisLabel>() }

    private val linePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
        }
    }

    private val linePath by lazy { Path() }
    var linePathEffect: PathEffect? = null
    var lineWidth = 3F
    var lineColor = Color.parseColor("#F0F0F0")
    private val lineList by lazy { mutableListOf<AxisLine>() }

    /**
     * 添加轴线
     */
    fun addLines(vararg lines: AxisLine) {
        lineList.addAll(lines)
    }

    /**
     * 添加标签
     */
    fun addLabels(vararg label: AxisLabel) {
        labelList.addAll(label)
    }

    /**
     * 设置坐标区域
     * @param contentBounds 内容边界
     */
    fun setBounds(contentBounds: RectF) {
        panelContentBounds = contentBounds
        var left = contentBounds.left
        var top = contentBounds.top
        var right = contentBounds.right
        var bottom = contentBounds.bottom
        when (axisSide) {
            AxisSide.LEFT -> {
                if (axisPosition == AxisPosition.OUTSIDE) {
                    left -= axisSize
                }
                right = left + axisSize
            }

            AxisSide.TOP -> {
                if (axisPosition == AxisPosition.OUTSIDE) {
                    top -= axisSize
                }
                bottom = top + axisSize
            }

            AxisSide.RIGHT -> {
                if (axisPosition == AxisPosition.OUTSIDE) {
                    right += axisSize
                }
                left = right - axisSize
            }

            AxisSide.BOTTOM -> {
                if (axisPosition == AxisPosition.OUTSIDE) {
                    bottom += axisSize
                }
                top = bottom - axisSize
            }
        }
        axisBounds.set(left, top, right, bottom)

        prepareToDraw()
    }

    /**
     * 绘制之前的一些准备工作
     */
    fun prepareToDraw() {
        prepareLines()
        prepareLabels()
    }

    /**
     * 开始绘制
     */
    fun startDraw(canvas: Canvas) {
        canvas.run {
            drawLines()
            drawLabels()
        }
    }

    // ========== 轴线相关 ==========

    /**
     * 轴线处理
     */
    private fun prepareLines() {
        val bounds = panelContentBounds ?: return

        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = lineWidth

        val left = bounds.left
        val right = bounds.right
        val top = bounds.top
        val bottom = bounds.bottom
        val width = bounds.width()
        val height = bounds.height()

        for (line in lineList) {
            when (axisSide) {
                AxisSide.LEFT, AxisSide.RIGHT -> {
                    // Y轴方向，画水平线
                    val y = bottom - height * line.ratio
                    line.startPoint.set(left, y)
                    line.endPoint.set(right, y)
                }

                AxisSide.TOP, AxisSide.BOTTOM -> {
                    // X轴方向，画垂直线
                    val x = left + width * line.ratio
                    line.startPoint.set(x, top)
                    line.endPoint.set(x, bottom)
                }
            }
        }
    }

    /**
     * 绘制虚线轴线 [lineList]
     */
    private fun Canvas.drawLines() {
        linePaint.apply {
            style = Paint.Style.STROKE
            strokeWidth = lineWidth
            color = lineColor
            pathEffect = linePathEffect
        }

        for (line in lineList) {
            linePath.reset()
            linePath.moveTo(line.startX(), line.startY())
            linePath.lineTo(line.endX(), line.endY())
            drawPath(linePath, linePaint)
        }

        linePaint.pathEffect = null // 清除 pathEffect，避免影响后续绘制
    }

    // ========== 标签相关 ==========

    /**
     * 标签 [labelList] 的一些准备工作
     */
    private fun prepareLabels() {
        when (axisSide) {
            AxisSide.LEFT, AxisSide.RIGHT -> prepareYLabels()
            AxisSide.TOP, AxisSide.BOTTOM -> prepareXLabels()
        }
    }

    /**
     * Y 轴标签处理
     */
    private fun prepareYLabels() {
        paint.textSize = labelTextSize

        val descent = paint.descent()
        val ascent = paint.ascent()

        // 计算标签的横向位置 x
        val x = when (axisSide) {
            AxisSide.LEFT -> if (axisPosition == AxisPosition.INSIDE) {
                axisBounds.left + horizontalMargin
            } else {
                axisBounds.right - horizontalMargin
            }

            AxisSide.RIGHT -> if (axisPosition == AxisPosition.INSIDE) {
                axisBounds.right - horizontalMargin
            } else {
                axisBounds.left + horizontalMargin
            }

            else -> axisBounds.left + horizontalMargin // 容错处理
        }

        val totalHeight = axisBounds.height()
        val bottom = axisBounds.bottom
        for (label in labelList) {
            val ratio = label.ratio
            val value = totalHeight * ratio

            if (label.isNeedCalculateText()) {
                label.text = formatter?.format(axisSide, axisPosition, value) ?: "$value"
            }

            val y = bottom - value
            label.y = when {
                ratio <= 0f -> y - descent            // 顶部对齐
                ratio >= 1f -> y - ascent             // 底部对齐
                else -> y - (ascent + descent) / 2f   // 居中对齐
            }

            label.x = x
        }
    }

    /**
     * X 轴标签处理
     */
    private fun prepareXLabels() {
        paint.textSize = labelTextSize

        val y = paint.centerY(axisBounds)
        labelList.forEach {
            val ratio = it.ratio
            val axisValue = axisBounds.width() * ratio
            if (it.isNeedCalculateText()) {
                it.text = formatter?.format(axisSide, axisPosition, axisValue) ?: "$axisValue"
            }
            it.x = axisBounds.left + axisValue
            it.x = when {
                ratio <= 0F -> it.x // 左边对齐轴线
                ratio >= 1F -> it.x - paint.textWidth(it.text)  // 右边对齐轴线
                else -> it.x - paint.textWidth(it.text) / 2 // 居中对齐
            }
            it.y = y
        }
    }

    /**
     * 绘制 标签 [labelList]
     */
    private fun Canvas.drawLabels() {
        paint.color = labelTextColor
        paint.textSize = labelTextSize
        paint.textAlign = when (axisSide) {
            AxisSide.LEFT -> if (axisPosition == AxisPosition.INSIDE) Paint.Align.LEFT else Paint.Align.RIGHT
            AxisSide.TOP, AxisSide.BOTTOM -> Paint.Align.LEFT
            AxisSide.RIGHT -> if (axisPosition == AxisPosition.INSIDE) Paint.Align.RIGHT else Paint.Align.LEFT
        }

        labelList.forEach {
            drawText(it.text.orEmpty(), it.x, it.y, paint)
        }
    }
}