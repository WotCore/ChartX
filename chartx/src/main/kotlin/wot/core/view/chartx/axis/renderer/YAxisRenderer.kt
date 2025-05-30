package wot.core.view.chartx.axis.renderer

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

/**
 * Y轴绘制实现，绘制刻度线和对应的数值
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
class YAxisRenderer : BaseAxisRenderer() {

    /**
     * 标签与轴之间的间距, 防止贴边
     */
    var margin = 8F

    override fun initLabel(rectF: RectF) {
        paint.reset()
        paint.textSize = labelTextSize

        val descent = paint.descent()
        val ascent = paint.ascent()
        val x = rectF.right - margin
        labelList.forEach {
            val y = rectF.bottom - rectF.height() * it.ratio
            it.y = when {
                it.ratio <= 0F -> y - descent // 顶部对齐
                it.ratio >= 1F -> y - ascent // 底部对齐
                else -> y - (ascent + descent) / 2 // 垂直居中
            }
            it.x = x
        }
    }

    override fun onDraw(canvas: Canvas) {
        paint.reset()
        paint.color = labelTextColor
        paint.textSize = labelTextSize
        paint.textAlign = Paint.Align.RIGHT

        labelList.forEach {
            canvas.drawText(it.text.orEmpty(), it.x, it.y, paint)
        }
    }
}