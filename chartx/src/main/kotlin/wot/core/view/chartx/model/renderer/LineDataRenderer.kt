package wot.core.view.chartx.model.renderer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import wot.core.view.chartx.model.data.ChartDataSet

/**
 * 折线图渲染器
 *
 * @author :
 * @date : 2025/6/3
 */
class LineDataRenderer(private var lineColor: Int = Color.BLUE) : BaseDataRenderer() {

    var lineWidth = 4F

    private val path by lazy { Path() }

    override fun onDraw(canvas: Canvas, paint: Paint, contentRectF: RectF, dataSet: ChartDataSet) {
        val entryList = dataSet.entryList
        path.reset()
        var isFirstPoint = true

        // 临时变量用于切换画笔样式
        val originalColor = paint.color
        val originalStyle = paint.style
        val originalStrokeWidth = paint.strokeWidth

        paint.strokeWidth = lineWidth
        paint.color = lineColor
        paint.style = Paint.Style.STROKE

        for (i in startIndex..endIndex) {
            if (i >= entryList.size) break
            val entry = entryList[i]

            val pixels = dataToPixels(i.toFloat(), entry.yValue)
            val x = pixels[0]
            val y = pixels[1]

            if (isFirstPoint) {
                path.moveTo(x, y)
                isFirstPoint = false
            } else {
                path.lineTo(x, y)
            }

            // 先保存当前状态
            val savedColor = paint.color
            val savedStyle = paint.style

            // 画圆圈
            paint.style = Paint.Style.FILL
            paint.color = Color.RED // 圆点颜色可以自定义
            canvas.drawCircle(x, y, 6f, paint)

            // 恢复画线状态
            paint.color = savedColor
            paint.style = savedStyle
        }

        // 绘制线条（最后统一画一次 path）
        canvas.drawPath(path, paint)

        // 可选：恢复画笔原状态
        paint.color = originalColor
        paint.style = originalStyle
        paint.strokeWidth = originalStrokeWidth
    }
}
