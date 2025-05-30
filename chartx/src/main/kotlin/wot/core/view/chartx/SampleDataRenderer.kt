package wot.core.view.chartx

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import wot.core.view.chartx.model.ChartEntry
import wot.core.view.chartx.renderer.BaseDataRenderer

/**
 * 功能说明
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
class SampleDataRenderer : BaseDataRenderer() {

    override fun onDraw(
        canvas: Canvas,
        paint: Paint,
        contentRectF: RectF,
        entryList: MutableList<ChartEntry>
    ) {
        paint.reset()
        paint.color = Color.GRAY
        canvas.drawRect(contentRectF, paint)
    }
}