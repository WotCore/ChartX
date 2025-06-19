package wot.core.view.chartx.axis

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import wot.core.view.chartx.axis.model.AxisLabel
import wot.core.view.chartx.axis.model.AxisPosition
import wot.core.view.chartx.axis.model.AxisSide

/**
 * Y 轴
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
class YAxis(
    side: AxisSide = AxisSide.LEFT, // 轴所在侧, 默认在左边
    position: AxisPosition = AxisPosition.OUTSIDE // 轴所在位置, 默认在外部
) : BaseAxis(side, position) {

    /**
     * 标签与轴之间的间距, 防止贴边
     */
    var margin = 8F

    override fun setBounds(contentRectF: RectF, axisSize: Float) {
        rectF.set(
            contentRectF.left - axisSize,
            contentRectF.top,
            contentRectF.left,
            contentRectF.bottom
        )
    }

    override fun onPrepareLabels(labelList: List<AxisLabel>) {
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

    override fun onDrawLabels(canvas: Canvas, labelList: List<AxisLabel>) {
        paint.reset()
        paint.color = labelTextColor
        paint.textSize = labelTextSize
        paint.textAlign = Paint.Align.RIGHT

        labelList.forEach {
            canvas.drawText(it.text.orEmpty(), it.x, it.y, paint)
        }
    }
}