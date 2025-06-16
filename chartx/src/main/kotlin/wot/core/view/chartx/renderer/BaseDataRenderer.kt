package wot.core.view.chartx.renderer

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import wot.core.view.chartx.log.Logcat
import wot.core.view.chartx.model.RenderContext
import kotlin.math.max
import kotlin.math.min

/**
 * 数据渲染器
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
abstract class BaseDataRenderer {

    /**
     *数据的起始索引
     */
    protected var startIndex: Int = 0
        private set

    /**
     *数据的结束索引
     */
    protected var endIndex: Int = 0
        private set

    /**
     * 绘制
     * @param canvas 画布
     * @param paint 画笔
     * @param contentRectF 内容区域
     * @param renderContext 渲染上下文
     */
    abstract fun onDraw(
        canvas: Canvas, paint: Paint, contentRectF: RectF, renderContext: RenderContext
    )

    /**
     * 计算渲染范围
     */
    fun calcRenderRange(panelStartIndex: Int, panelEndIndex: Int, entryMaxIndex: Int) {
        this.startIndex = max(panelStartIndex, 0)
        this.endIndex = min(panelEndIndex, entryMaxIndex)
        Logcat.i("startIndex = $startIndex, endIndex = $endIndex")
    }
}