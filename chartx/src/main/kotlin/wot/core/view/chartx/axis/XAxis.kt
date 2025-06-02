package wot.core.view.chartx.axis

import android.graphics.Canvas
import android.graphics.RectF
import wot.core.view.chartx.axis.renderer.BaseAxisRenderer

/**
 * X 轴
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
class XAxis(renderer: BaseAxisRenderer) : BaseAxis(renderer) {

    override fun setBounds(contentRectF: RectF, axisSize: Float) {
        rectF.set(
            contentRectF.left,
            contentRectF.bottom,
            contentRectF.right,
            contentRectF.bottom + axisSize
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