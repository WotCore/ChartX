package wot.core.view.chartx.renderer

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import wot.core.view.chartx.log.Logcat
import wot.core.view.chartx.model.ChartDataManager
import wot.core.view.chartx.model.ChartViewport
import kotlin.math.max
import kotlin.math.min

/**
 * 数据渲染器 基类
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

    val dataManager by lazy { ChartDataManager() }

    /**
     * 绘制
     * @param canvas 画布
     * @param paint 画笔
     * @param contentRectF 内容区域
     */
    abstract fun onDraw(canvas: Canvas, paint: Paint, contentRectF: RectF)

    /**
     * 开始渲染
     */
    fun startRenderer(
        canvas: Canvas,
        paint: Paint,
        viewport: ChartViewport,
        contentRectF: RectF,
        pointRealWidth: Float // 点的真实宽度
    ) {
        val panelStartIndex = viewport.startIndex
        val panelEndIndex = viewport.startIndex
        // 坐标映射
        dataManager.buildValueToPixelMatrix(contentRectF, panelStartIndex, pointRealWidth)
        // 计算渲染实际索引
        calcRenderRange(panelStartIndex, panelEndIndex, dataManager.entryMaxIndex())
        // 绘制
        onDraw(canvas, paint, contentRectF)
    }

    /**
     * 计算渲染范围
     */
    private fun calcRenderRange(panelStartIndex: Int, panelEndIndex: Int, entryMaxIndex: Int) {
        this.startIndex = max(panelStartIndex, 0)
        this.endIndex = min(panelEndIndex, entryMaxIndex)
        Logcat.i("startIndex = $startIndex, endIndex = $endIndex")
    }
}