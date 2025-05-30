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
class YAxis(renderer: BaseAxisRenderer, width: Float) : BaseAxis(renderer, width = width) {

    override fun setBounds(contentRectF: RectF) {
        height = contentRectF.height()
        rectF.set(
            contentRectF.left - width,
            contentRectF.top,
            contentRectF.left,
            contentRectF.bottom
        )

        renderer.initLabel(rectF)
    }

    override fun notifyDataChanged() {
        renderer.initLabel(rectF)
    }

    override fun onDraw(canvas: Canvas) {
        renderer.onDraw(canvas)
    }
}