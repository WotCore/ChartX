package wot.core.view.chartx.model.renderer

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import wot.core.view.chartx.model.data.ChartDataSet
import wot.core.view.chartx.model.data.ChartEntry
import wot.core.view.chartx.model.transform.ChartValueMapper
import wot.core.view.chartx.model.viewport.ChartViewport
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

    private val dataSet by lazy { ChartDataSet() }
    private val valueMapper by lazy { ChartValueMapper() }
    private val buffer = FloatArray(2)

    val entryCount: Int
        get() = dataSet.entryCount()

    val lastIndex: Int
        get() = dataSet.lastIndex()

    /**
     * 绘制
     * @param canvas 画布
     * @param paint 画笔
     * @param contentRectF 内容区域
     * @param dataSet 图表数据管理器
     */
    abstract fun onDraw(canvas: Canvas, paint: Paint, contentRectF: RectF, dataSet: ChartDataSet)

    /**
     * 根据索引获取指定的 entry
     */
    fun entryAt(index: Int) = dataSet.entryAt(index)

    fun setNewData(newEntries: List<ChartEntry>) {
        dataSet.setNewData(newEntries)
    }

    /**
     * 绘制前的工作
     */
    fun prepareToDraw(viewport: ChartViewport, contentRect: RectF) {
        val startIndex = viewport.startIndex
        val endIndex = viewport.endIndex
        val pointWidth = viewport.pointRealWidth

        dataSet.computeYBounds(startIndex, endIndex)

        val yMin = dataSet.yMin
        val yRange = dataSet.yRange
        valueMapper.buildMatrix(contentRect, startIndex, pointWidth, yMin, yRange) // 坐标映射
    }

    /**
     * 开始绘制
     */
    fun startDraw(canvas: Canvas, paint: Paint, viewport: ChartViewport, contentRect: RectF) {
        // 计算渲染实际索引
        calcRenderRange(viewport.startIndex, viewport.endIndex, dataSet.lastIndex())
        // 绘制
        onDraw(canvas, paint, contentRect, dataSet)
    }

    /**
     * 根据 x 坐标获取对应的 entry
     */
    fun findEntryByX(x: Float): ChartEntry? {
        return entryAt(findIndexByX(x))
    }

    /**
     * 根据 x 坐标获取对应的索引
     */
    fun findIndexByX(x: Float): Int {
        buffer[0] = x
        pixelsToData(buffer)
        return buffer[0].toInt()
    }

    /**
     * 根据 y 坐标获取对应的值
     */
    fun findValueByY(y: Float): Float {
        buffer[1] = y
        pixelsToData(buffer)
        return buffer[1]
    }

    /**
     * 值数据映射成像素数据
     */
    fun dataToPixels(xValue: Float, yValue: Float): FloatArray {
        buffer[0] = xValue
        buffer[1] = yValue
        dataToPixels(buffer)
        return buffer
    }

    /**
     * 值数据映射成像素数据
     */
    fun dataToPixels(data: FloatArray) {
        valueMapper.toPixels(data)
    }

    /**
     * 坐标像素映射成数据值
     */
    fun pixelsToData(pixels: FloatArray) {
        valueMapper.toData(pixels)
    }

    /**
     * 计算渲染范围
     */
    private fun calcRenderRange(panelStartIndex: Int, panelEndIndex: Int, entryMaxIndex: Int) {
        this.startIndex = max(panelStartIndex, 0)
        this.endIndex = min(panelEndIndex, entryMaxIndex)
    }
}