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
abstract class BaseAxisRenderer() {

    protected val paint: Paint by lazy { Paint() }

    protected val labelList by lazy { mutableListOf<AxisLabel>() }
    var labelTextSize: Float = 20F
    var labelTextColor: Int = Color.BLACK

    /**
     * 初始化坐标标签
     * @param rectF 坐标轴区域
     */
    abstract fun initLabel(rectF: RectF)

    abstract fun onDraw(canvas: Canvas)

    fun addLabels(vararg label: AxisLabel) {
        labelList.addAll(label)
    }
}
