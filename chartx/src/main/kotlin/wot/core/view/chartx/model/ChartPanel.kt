package wot.core.view.chartx.model

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import wot.core.view.chartx.axis.XAxis
import wot.core.view.chartx.axis.YAxis
import wot.core.view.chartx.renderer.BaseDataRenderer

/**
 * 图表面板
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
data class ChartPanel(val viewport: ChartViewport) {

    /**
     *  面板区域
     */
    private val panelRectF by lazy { RectF() }

    /**
     * 内容区域( [panelRectF] 除去 [yAxis] 和 [xAxis] 剩余区域)
     */
    private val contentRectF by lazy { RectF() }

    private val dataRendererList by lazy { mutableListOf<BaseDataRenderer>() } // 数据渲染器列表

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
     * 绘制图表元素
     */
    fun drawChartElements(canvas: Canvas, paint: Paint) {
        xAxis?.startDraw(canvas)
        yAxis?.startDraw(canvas)

        val pointRealWidth = viewport.getPointRealWidth()
        dataRendererList.forEach {
            it.startRenderer(canvas, paint, viewport, contentRectF, pointRealWidth)
        }
    }

    /**
     * 添加数据渲染器
     */
    fun addDataRenderers(vararg renderers: BaseDataRenderer) {
        for (renderer in renderers) {
            dataRendererList.add(renderer)
        }
    }

    /**
     * 设置新数据
     * @param dataIndex 索引, 需要将数据设置到哪个渲染器
     */
    fun setNewData(dataIndex: Int = 0, entryList: List<ChartEntry>) {
        val renderer = dataRendererList.getOrNull(dataIndex) ?: return
        renderer.dataManager.setNewData(entryList)

        // 更新最大索引
        viewport.updateMaxIndex(renderer.dataManager.entryMaxIndex())
        val startIndex = viewport.startIndex
        val pointWidth = viewport.pointMaxWidth
        renderer.dataManager.buildValueToPixelMatrix(contentRectF, startIndex, pointWidth)

        xAxis?.prepareToDraw()
        yAxis?.prepareToDraw()
    }

    /**
     * 刷新索引范围
     */
    private fun updateIndexRange() {
        dataRendererList.forEach { renderer ->
            // 更新最大索引
            val dataManager = renderer.dataManager
            viewport.updateMaxIndex(dataManager.entryMaxIndex())
            val panelStartIndex = viewport.startIndex
            val panelEndIndex = viewport.pointMaxWidth
            dataManager.buildValueToPixelMatrix(contentRectF, panelStartIndex, panelEndIndex)
        }
    }
}