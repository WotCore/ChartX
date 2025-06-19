package wot.core.view.chartx.axis

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import wot.core.view.chartx.axis.formatter.AxisLabelFormatter
import wot.core.view.chartx.axis.model.AxisLabel
import wot.core.view.chartx.axis.model.AxisPosition
import wot.core.view.chartx.axis.model.AxisSide

/**
 * 功能说明
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
abstract class BaseAxis(
    val side: AxisSide,
    val position: AxisPosition,
) {
    var labelTextSize: Float = 20F
    var labelTextColor: Int = Color.BLACK

    var formatter: AxisLabelFormatter? = null

    /**
     * 绘制区域的坐标
     */
    protected val rectF by lazy { RectF() }

    protected val paint: Paint by lazy { Paint() }

    private val labelList by lazy { mutableListOf<AxisLabel>() }

    /**
     * 设置绘制区域边界
     * @param contentRectF 内容区域
     * @param axisSize 坐标轴大小
     */
    abstract fun setBounds(contentRectF: RectF, axisSize: Float)

    /**
     * 标签 [labelList] 的一些准备工作
     */
    abstract fun onPrepareLabels(labelList: List<AxisLabel>)

    /**
     * 绘制 标签 [labelList]
     */
    abstract fun onDrawLabels(canvas: Canvas, labelList: List<AxisLabel>)

    /**
     * 绘制之前的一些准备工作
     */
    fun prepareToDraw() {
        onPrepareLabels(labelList)
    }

    /**
     * 开始绘制
     */
    fun startDraw(canvas: Canvas) {
        onDrawLabels(canvas, labelList)
    }

    /**
     * 添加 label
     */
    fun addLabels(vararg label: AxisLabel) {
        labelList.addAll(label)
    }
}