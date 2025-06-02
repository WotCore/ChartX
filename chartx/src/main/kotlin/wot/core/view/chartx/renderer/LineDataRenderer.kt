package wot.core.view.chartx.renderer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import wot.core.view.chartx.model.RenderContext

/**
 * 折线图渲染器
 *
 * @author :
 * @date : 2025/6/3
 */
class LineDataRenderer(var lineColor: Int = Color.BLUE) : BaseDataRenderer() {

    var lineWidth = 4F

    private val path by lazy { Path() }
    private val lineBuffer by lazy { FloatArray(2) }

    override fun onDraw(
        canvas: Canvas,
        paint: Paint,
        contentRectF: RectF,
        pointWidth: Float,
        renderContext: RenderContext
    ) {
        val entries = renderContext.entryList
        if (entries.isEmpty() || startIndex > endIndex || startIndex >= entries.size) return

        path.reset()
        var isFirstPoint = true
        for (i in startIndex..endIndex) {
            if (i >= entries.size) break
            val entry = entries[i]
            lineBuffer[0] = i + 0.5f
            lineBuffer[1] = entry.yValue
            renderContext.mapPointValuesToPixel(lineBuffer)

            if (isFirstPoint) {
                path.moveTo(lineBuffer[0], lineBuffer[1])
                isFirstPoint = false
            } else {
                path.lineTo(lineBuffer[0], lineBuffer[1])
            }
        }

        paint.style = Paint.Style.STROKE
        paint.color = lineColor
        paint.strokeWidth = lineWidth
        canvas.drawPath(path, paint)
    }
}
