package wot.core.view.chartx.axis.renderer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import wot.core.view.chartx.ext.centerY

/**
 * X轴绘制实现，绘制刻度线和对应的数值
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
class XAxisRenderer : BaseAxisRenderer() {

    override fun onCalcLabel(rectF: RectF) {
        paint.reset()
        paint.color = labelTextColor
        paint.textSize = labelTextSize
        paint.textAlign = Paint.Align.LEFT

        val y = paint.centerY(rectF)
        labelList.forEach {
            val text = it.text.orEmpty()
            val ratio = it.ratio
            it.x = rectF.left + rectF.width() * ratio
            it.x = if (ratio >= 1f) it.x - paint.measureText(text) else it.x
            it.y = y
        }
    }

    override fun onDrawLabel(canvas: Canvas) {
        paint.reset()
        paint.color = labelTextColor
        paint.textSize = labelTextSize
        paint.textAlign = Paint.Align.LEFT

        labelList.forEach {
            canvas.drawText(it.text.orEmpty(), it.x, it.y, paint)
        }
    }
}