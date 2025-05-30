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
class XAxis(renderer: BaseAxisRenderer, height: Float) : BaseAxis(renderer, height = height) {

    override fun setBounds(contentRectF: RectF) {
        width = contentRectF.width()
        rectF.set(
            contentRectF.left,
            contentRectF.bottom,
            contentRectF.right,
            contentRectF.bottom + height
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