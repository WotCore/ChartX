package wot.core.view.chartx

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import wot.core.view.chartx.model.ChartEntry
import wot.core.view.chartx.model.ChartPanel

/**
 * 图表基类
 * 1. 将控件分成多份面板, 每份面板单独绘制
 *
 * @author : yangsn
 * @date : 2025/5/28
 */
abstract class BaseChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val chartPanelsList by lazy { createChartPanel() }

    /**
     * 创建图表面板
     */
    abstract fun createChartPanel(): MutableList<ChartPanel>

    /**
     * 控件大小变更, 绘制区域大小重新调整
     * @param panelList 面板列表
     * @param viewWidth 控件宽度
     * @param viewHeight 控件高度
     */
    abstract fun onSizeChanged(panelList: MutableList<ChartPanel>, viewWidth: Int, viewHeight: Int)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        onSizeChanged(chartPanelsList, w, h)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (panel in chartPanelsList) {
            panel.onDraw(canvas, paint)
        }
    }

    /**
     * 设置新数据
     * @param regionIndex 绘制区域索引
     */
    fun setNewData(dataList: List<ChartEntry>, regionIndex: Int = 0, invalidate: Boolean = true) {
        chartPanelsList[regionIndex].setNewData(dataList)
        if (invalidate) {
            invalidate()
        }
    }
}