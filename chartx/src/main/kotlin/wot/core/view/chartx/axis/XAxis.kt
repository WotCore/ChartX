package wot.core.view.chartx.axis

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import wot.core.view.chartx.axis.model.AxisLabel
import wot.core.view.chartx.axis.model.AxisPosition
import wot.core.view.chartx.axis.model.AxisSide
import wot.core.view.chartx.ext.centerY

/**
 * X 轴
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
class XAxis(
    side: AxisSide = AxisSide.BOTTOM, // 轴所在侧, 默认在底部
    position: AxisPosition = AxisPosition.OUTSIDE // 轴所在位置, 默认在外部
) : BaseAxis(side, position) {

    override fun setBounds(contentRectF: RectF, axisSize: Float) {
        rectF.set(
            contentRectF.left,
            contentRectF.bottom,
            contentRectF.right,
            contentRectF.bottom + axisSize
        )

        prepareToDraw()
    }

    override fun onPrepareLabels(labelList: List<AxisLabel>) {
        paint.reset()
        paint.color = labelTextColor
        paint.textSize = labelTextSize
        paint.textAlign = Paint.Align.LEFT

        val y = paint.centerY(rectF)
        labelList.forEach {
            val ratio = it.ratio
            val axisValue = rectF.width() * ratio
            val text = if (it.isNeedCalculateText()) {
                formatter?.format(side, position, axisValue) ?: "$axisValue"
            } else {
                it.text
            }

            it.x = rectF.left + axisValue
            it.x = if (ratio >= 1f) it.x - paint.measureText(text) else it.x
            it.y = y
        }
    }

    override fun onDrawLabels(canvas: Canvas, labelList: List<AxisLabel>) {
        paint.reset()
        paint.color = labelTextColor
        paint.textSize = labelTextSize
        paint.textAlign = Paint.Align.LEFT

        labelList.forEach {
            canvas.drawText(it.text.orEmpty(), it.x, it.y, paint)
        }
    }
}