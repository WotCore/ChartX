package wot.core.view.chartx.model

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import wot.core.view.chartx.axis.XAxis
import wot.core.view.chartx.axis.YAxis
import wot.core.view.chartx.log.Logcat
import wot.core.view.chartx.renderer.BaseDataRenderer

/**
 * 面板
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
data class Panel(val viewport: Viewport) {

    /**
     * 面板区域
     */
    private val panelRectF by lazy { RectF() }

    /**
     * 内容区域( [panelRectF] 除去 [yAxis] 和 [xAxis] 剩余区域)
     */
    private val contentRectF by lazy { RectF() }

    /**
     * 渲染器上下文列表
     */
    private val renderContextList by lazy { mutableListOf<RenderContext>() }

    var xAxis: XAxis? = null
    var yAxis: YAxis? = null

    /**
     * 设置面板区域边界
     */
    fun setBounds(
        left: Float, top: Float, right: Float, bottom: Float,
        xAxisHeight: Float = 0F, // X轴高度
        yAxisWidth: Float = 0F, // Y轴宽度
    ) {
        panelRectF.set(left, top, right, bottom)
        val top = panelRectF.top
        val right = panelRectF.right
        val left = panelRectF.left + xAxisHeight
        val bottom = panelRectF.bottom - yAxisWidth
        contentRectF.set(left, top, right, bottom)

        // 设置坐标绘制区域边界
        xAxis?.setBounds(contentRectF, xAxisHeight)
        yAxis?.setBounds(contentRectF, yAxisWidth)

        updateIndexRange() // 刷新索引范围
    }

    /**
     * 绘制元素
     */
    fun drawElements(canvas: Canvas, paint: Paint) {
        xAxis?.onDraw(canvas)
        yAxis?.onDraw(canvas)

        val panelStartIndex = viewport.startIndex
        val panelEndIndex = viewport.endIndex
        renderContextList.forEach {
            it.prepareValueToPxMatrix(contentRectF, panelStartIndex, viewport.getPointRealWidth())
            // 先计算渲染实际索引
            it.renderer.calcRenderRange(panelStartIndex, panelEndIndex, it.entryMaxIndex())
            // 再进行绘制
            it.renderer.onDraw(canvas, paint, contentRectF, it)
        }
    }

    /**
     * 添加渲染器
     */
    fun addRenderers(vararg renderers: BaseDataRenderer) {
        for (renderer in renderers) {
            renderContextList.add(RenderContext(renderer))
        }
    }

    /**
     * 设置新数据
     * @param dataIndex 索引, 需要将数据设置到哪个渲染器
     */
    fun setNewData(dataIndex: Int = 0, entryList: List<ChartEntry>) {
        val renderContext = renderContextList.getOrNull(dataIndex) ?: return
        renderContext.setNewData(entryList)

        // 更新最大索引
        viewport.updateMaxIndex(renderContext.entryMaxIndex())
        val startIndex = viewport.startIndex
        val pointWidth = viewport.pointMaxWidth
        renderContext.prepareValueToPxMatrix(contentRectF, startIndex, pointWidth)

        xAxis?.updateMetrics()
        yAxis?.updateMetrics()
    }

    /**
     * 刷新索引范围
     */
    private fun updateIndexRange() {
        renderContextList.forEach { renderContext ->
            // 更新最大索引
            viewport.updateMaxIndex(renderContext.entryMaxIndex())
            val startIndex = viewport.startIndex
            val pointWidth = viewport.pointMaxWidth
            renderContext.prepareValueToPxMatrix(contentRectF, startIndex, pointWidth)
        }
    }
}