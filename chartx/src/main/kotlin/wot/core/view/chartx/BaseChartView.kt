package wot.core.view.chartx

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import wot.core.view.chartx.model.ChartEntry
import wot.core.view.chartx.model.Panel
import wot.core.view.chartx.model.Viewport
import wot.core.view.chartx.touch.GestureHandler
import wot.core.view.chartx.touch.OnTouchListener

/**
 * 图表基类
 * 1. 将控件分成多份面板, 每份面板单独绘制
 *
 * @author : yangsn
 * @date : 2025/5/28
 */
abstract class BaseChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), OnTouchListener {

    private val viewport by lazy { Viewport() }

    private val gestureHandler by lazy {
        GestureHandler().apply {
            onTouchListener = this@BaseChartView
        }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val panelList by lazy { createPanels(viewport) }

    private var accumulatedDeltaX = 0f // 累计移动的距离

    /**
     * 创建面板列表
     */
    abstract fun createPanels(viewport: Viewport): MutableList<Panel>

    /**
     * 更新面板边界
     * @param panelList 面板列表
     * @param viewWidth 控件宽度
     * @param viewHeight 控件高度
     */
    abstract fun updatePanelBounds(panelList: MutableList<Panel>, viewWidth: Int, viewHeight: Int)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewport.setContentWidth(w.toFloat() - paddingStart - paddingEnd)
        // 更新面板边界
        updatePanelBounds(panelList, w, h)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 绘制元素
        panelList.forEach { it.drawElements(canvas, paint) }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureHandler.handleTouchEvent(event)
    }

    override fun onDown(x: Float, y: Float) {
        accumulatedDeltaX = 0f
    }

    override fun onMove(x: Float, y: Float, deltaX: Float, deltaY: Float) {
        accumulatedDeltaX += deltaX
        val pointWidth = viewport.getPointRealWidth()
        if (kotlin.math.abs(accumulatedDeltaX) >= pointWidth) {
            val moveCount = (accumulatedDeltaX / pointWidth).toInt() // 计算移动的整点数
            val moveDistance = moveCount * pointWidth // 计算实际移动的距离（整点数 × 点宽度）
            viewport.panVisibleRange(moveCount)
            invalidate()
            accumulatedDeltaX -= moveDistance  // 减去已经消费的距离，保留剩余部分
        }
    }

    override fun onUp(x: Float, y: Float) {
        accumulatedDeltaX = 0f
    }

    /**
     * 设置新数据
     * @param triples 面板数据, 三元组(画板索引, 数据索引, 数据列表)
     */
    fun setNewData(
        vararg triples: Triple<Int, Int, List<ChartEntry>>,
        invalidate: Boolean = true
    ) {
        triples.forEach { (panelIndex, dataIndex, dataList) ->
            val panel = panelList.getOrNull(panelIndex) ?: return@forEach
            panel.setNewData(dataIndex, dataList)
        }
        if (invalidate) {
            invalidate()
        }
    }

    /**
     * 设置新数据
     * @param panelIndex 画板索引
     * @param dataIndex 数据索引
     * @param dataList 数据列表
     */
    fun setNewData(
        panelIndex: Int = 0,
        dataIndex: Int = 0,
        dataList: List<ChartEntry>,
        invalidate: Boolean = true
    ) {
        val panel = panelList.getOrNull(panelIndex) ?: return
        panel.setNewData(dataIndex, dataList)
        if (invalidate) {
            invalidate()
        }
    }
}