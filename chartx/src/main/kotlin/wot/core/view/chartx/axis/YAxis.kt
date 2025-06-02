package wot.core.view.chartx.axis

import android.graphics.Canvas
import android.graphics.RectF
import wot.core.view.chartx.axis.renderer.BaseAxisRenderer

/**
 * Y 轴
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
class YAxis(renderer: BaseAxisRenderer) : BaseAxis(renderer) {

    override fun setBounds(contentRectF: RectF, axisSize: Float) {
        rectF.set(
            contentRectF.left - axisSize,
            contentRectF.top,
            contentRectF.left,
            contentRectF.bottom
        )

        renderer.updateMetrics(rectF)
    }

    override fun onDraw(canvas: Canvas) {
        renderer.onDraw(canvas)
    }

    override fun updateMetrics() {
        renderer.updateMetrics(rectF)
    }
}