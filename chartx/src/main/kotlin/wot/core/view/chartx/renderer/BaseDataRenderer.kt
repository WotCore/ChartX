package wot.core.view.chartx.renderer

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import wot.core.view.chartx.model.ChartEntry

/**
 * 数据渲染器
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
abstract class BaseDataRenderer {

    abstract fun onDraw(
        canvas: Canvas, paint: Paint, contentRectF: RectF, entryList: MutableList<ChartEntry>
    )
}