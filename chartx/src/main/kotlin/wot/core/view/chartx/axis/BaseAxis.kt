package wot.core.view.chartx.axis

import android.graphics.Canvas
import android.graphics.RectF
import wot.core.view.chartx.axis.renderer.IBaseAxisRenderer

/**
 * 功能说明
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
abstract class BaseAxis(var renderer: IBaseAxisRenderer) {

    /**
     * 绘制区域的坐标
     */
    val rectF by lazy { RectF() }

    /**
     * 设置绘制区域边界
     * @param contentRectF 内容区域
     * @param axisSize 坐标轴大小
     */
    abstract fun setBounds(contentRectF: RectF, axisSize: Float)

    /**
     * 绘制
     */
    abstract fun onDraw(canvas: Canvas)

    /**
     * 更新坐标轴的显示指标
     */
    abstract fun updateMetrics()
}