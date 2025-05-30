package wot.core.view.chartx.axis

import android.graphics.Canvas
import android.graphics.RectF
import wot.core.view.chartx.axis.renderer.BaseAxisRenderer

/**
 * 功能说明
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
abstract class BaseAxis(var renderer: BaseAxisRenderer, var width: Float = 0F, var height: Float = 0F) {

    /**
     * 绘制区域的坐标
     */
    val rectF by lazy { RectF() }

    /**
     * 设置绘制区域边界
     * @param contentRectF 内容区域
     */
    abstract fun setBounds(contentRectF: RectF)

    /**
     * 通知数据变更, 在下面做一些逻辑处理
     */
    abstract fun notifyDataChanged()

    /**
     * 绘制
     */
    abstract fun onDraw(canvas: Canvas)
}