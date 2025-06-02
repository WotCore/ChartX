package wot.core.view.chartx.axis.renderer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import wot.core.view.chartx.axis.model.AxisLabel

/**
 * 坐标轴基类
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
abstract class BaseAxisRenderer : IBaseAxisRenderer {

    protected val paint: Paint by lazy { Paint() }

    protected val labelList by lazy { mutableListOf<AxisLabel>() }
    var labelTextSize: Float = 20F
    var labelTextColor: Int = Color.BLACK

    /**
     * 更新坐标轴的显示指标(坐标值、)
     * @param rectF 坐标轴区域
     */
    override fun updateMetrics(rectF: RectF) {
        onCalcLabel(rectF)
    }

    override fun onDraw(canvas: Canvas) {
        onDrawLabel(canvas)
    }

    override fun addLabels(vararg label: AxisLabel) {
        labelList.addAll(label)
    }

    /**
     * 计算坐标标签
     * @param rectF 坐标轴区域
     */
    abstract fun onCalcLabel(rectF: RectF)

    /**
     * 绘制标签
     */
    abstract fun onDrawLabel(canvas: Canvas)
}

interface IBaseAxisRenderer {

    /**
     * 更新坐标轴的显示指标(坐标值、)
     * @param rectF 坐标轴区域
     */
    fun updateMetrics(rectF: RectF)

    /**
     * 绘制数据
     */
    fun onDraw(canvas: Canvas)

    /**
     * 增加标签
     */
    fun addLabels(vararg label: AxisLabel)
}
