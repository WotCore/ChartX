package wot.core.view.chartx.model.data

import wot.core.view.chartx.ext.minMaxInRange
import wot.core.view.chartx.utils.MathUtils
import kotlin.math.min

/**
 * 图表数据管理器（负责数据的收集与最值计算）
 *
 * @author : yangsn
 * @date : 2025/6/20
 */
class ChartDataSet {

    val entryList: List<ChartEntry> get() = _entryList

    var yMin: Float = Float.MAX_VALUE
        private set
    var yMax: Float = Float.MIN_VALUE
        private set

    /**
     * 表示Y轴范围放大比例 (防止数据贴边)
     */
    var yRangeScale = 1.2F

    val yRange: Float
        get() = yMax - yMin

    private val _entryList = mutableListOf<ChartEntry>()

    fun setNewData(newEntries: List<ChartEntry>) {
        _entryList.clear()
        _entryList.addAll(newEntries)
    }

    fun entryCount() = _entryList.size

    fun entryAt(index: Int): ChartEntry? = _entryList.getOrNull(index)

    fun lastIndex() = entryCount() - 1

    /**
     * 计算 Y 边界值
     */
    fun computeYBounds(startIndex: Int, endIndex: Int) {
        val end = min(endIndex, lastIndex())
        val minMax = _entryList.minMaxInRange(startIndex, end) { it.yValue }
        if (minMax == null) {
            yMin = Float.MAX_VALUE
            yMax = Float.MIN_VALUE
        } else {
            yMin = minMax.first.yValue
            yMax = minMax.second.yValue
        }
        val (minValue, maxValue) = MathUtils.expandRange(yMin, yMax, yRangeScale) // 放大比例 (防止数据贴边)
        yMin = minValue
        yMax = maxValue
    }
}
