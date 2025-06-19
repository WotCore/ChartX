package wot.core.view.chartx.model

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import wot.core.view.chartx.axis.ChartAxis
import wot.core.view.chartx.axis.model.AxisPosition
import wot.core.view.chartx.axis.model.AxisSide
import wot.core.view.chartx.log.Logcat
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
    private val panelBounds by lazy { RectF() }

    /**
     * 内容区域( [panelBounds] 除去 坐标轴[axisList] 所有 [AxisPosition.OUTSIDE] 坐标轴宽度剩余区域)
     */
    private val contentBounds by lazy { RectF() }

    private val dataRendererList by lazy { mutableListOf<BaseDataRenderer>() } // 数据渲染器列表

    private val axisList by lazy { mutableListOf<ChartAxis>() }

    /**
     * 设置面板区域边界
     */
    fun setBounds(left: Float, top: Float, right: Float, bottom: Float) {
        panelBounds.set(left, top, right, bottom)

        var cTop = panelBounds.top
        var cRight = panelBounds.right
        var cLeft = panelBounds.left
        var cBottom = panelBounds.bottom
        // 减掉坐标轴区域
        val outsideSizes = getOutsideAxisSizes()
        for ((axisSide, axisSize) in outsideSizes) {
            when (axisSide) {
                AxisSide.LEFT -> cLeft += axisSize
                AxisSide.TOP -> cTop += axisSize
                AxisSide.RIGHT -> cRight -= axisSize
                AxisSide.BOTTOM -> cBottom -= axisSize
            }
        }
        contentBounds.set(cLeft, cTop, cRight, cBottom)

        // 设置坐标绘制区域边界
        axisList.forEach {
            it.setBounds(contentBounds)
        }

        updateIndexRange() // 刷新索引范围
    }

    /**
     * 绘制图表元素
     */
    fun drawChartElements(canvas: Canvas, paint: Paint) {
        axisList.forEach {
            it.startDraw(canvas)
        }

        val pointRealWidth = viewport.getPointRealWidth()
        dataRendererList.forEach {
            it.startRenderer(canvas, paint, viewport, contentBounds, pointRealWidth)
        }
    }

    /**
     * 添加坐标轴，如果已有相同 axisSide + axisPosition，则忽略。
     */
    fun addAxis(vararg chartAxes: ChartAxis) {
        chartAxes.forEach { axis ->
            val exists = axisList.any {
                it.axisSide == axis.axisSide && it.axisPosition == axis.axisPosition
            }
            if (exists) {
                Logcat.w("${axis.axisSide} ${axis.axisPosition} axis already exists")
            } else {
                axisList.add(axis)
            }
        }
    }

    /**
     * 添加坐标轴，如果已有相同 axisSide + axisPosition，则替换。
     */
    fun addOrReplaceAxis(axis: ChartAxis) {
        val index = axisList.indexOfFirst {
            it.axisSide == axis.axisSide && it.axisPosition == axis.axisPosition
        }
        if (index != -1) {
            axisList[index] = axis
        } else {
            axisList.add(axis)
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
     * @param dataIndex 索引，需要将数据设置到哪个渲染器。
     */
    fun setNewData(dataIndex: Int = 0, entryList: List<ChartEntry>) {
        val renderer = dataRendererList.getOrNull(dataIndex) ?: return
        renderer.dataManager.setNewData(entryList)

        // 更新最大索引
        viewport.updateMaxIndex(renderer.dataManager.entryMaxIndex())
        val startIndex = viewport.startIndex
        val pointWidth = viewport.pointMaxWidth
        renderer.dataManager.buildValueToPixelMatrix(contentBounds, startIndex, pointWidth)

        axisList.forEach {
            it.prepareToDraw()
        }
    }

    /**
     * 获取内容宽度
     */
    fun getContentWidth() = contentBounds.width()

    /**
     * 取出 坐标轴[axisList] 所有 [AxisPosition.OUTSIDE] 的最大 axisSize
     */
    private fun getOutsideAxisSizes(): Map<AxisSide, Float> {
        return axisList
            .filter { it.axisPosition == AxisPosition.OUTSIDE } // 找出 [AxisPosition.OUTSIDE]
            .associate { it.axisSide to it.axisSize } // 把集合转成 map
    }

    /**
     * 刷新索引范围。
     */
    private fun updateIndexRange() {
        dataRendererList.forEach { renderer ->
            // 更新最大索引
            val dataManager = renderer.dataManager
            viewport.updateMaxIndex(dataManager.entryMaxIndex())
            val panelStartIndex = viewport.startIndex
            val panelEndIndex = viewport.pointMaxWidth
            dataManager.buildValueToPixelMatrix(contentBounds, panelStartIndex, panelEndIndex)
        }
    }
}