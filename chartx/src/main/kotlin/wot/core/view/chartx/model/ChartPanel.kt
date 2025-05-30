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
data class ChartPanel(
    var yAxis: YAxis? = null, // Y 轴
    var xAxis: XAxis? = null, // X 轴
    val dataRenderer: BaseDataRenderer,
) {

    /**
     * 绘制的真实区域
     */
    private val drawRegionRectF by lazy { RectF() }

    /**
     * 内容区域( [drawRegionRectF] 除去 [yAxis] 和 [xAxis] 剩余区域)
     */
    private val contentRectF by lazy { RectF() }

    /**
     * 面板绑定数据
     */
    private val entryList by lazy { mutableListOf<ChartEntry>() }

    /**
     * 设置绘制区域边界
     */
    fun setBounds(left: Float, top: Float, right: Float, bottom: Float) {
        drawRegionRectF.set(left, top, right, bottom)
        val top = drawRegionRectF.top
        val right = drawRegionRectF.right
        val left = drawRegionRectF.left + if (yAxis == null) 0F else yAxis!!.width
        val bottom = drawRegionRectF.bottom - if (xAxis == null) 0F else xAxis!!.height
        contentRectF.set(left, top, right, bottom)

        // 设置坐标绘制区域边界
        xAxis?.setBounds(contentRectF)
        yAxis?.setBounds(contentRectF)
    }

    fun onDraw(canvas: Canvas, paint: Paint) {
        xAxis?.onDraw(canvas)
        yAxis?.onDraw(canvas)
        dataRenderer.onDraw(canvas, paint, contentRectF, entryList)
    }

    fun setNewData(data: List<ChartEntry>) {
        entryList.clear()
        entryList.addAll(data)
        notifyDataChanged()
    }

    fun notifyDataChanged() {
        xAxis?.notifyDataChanged()
        yAxis?.notifyDataChanged()
    }
}