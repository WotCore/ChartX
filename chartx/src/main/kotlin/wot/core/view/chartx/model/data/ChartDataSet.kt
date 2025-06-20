package wot.core.view.chartx.model.data

import wot.core.view.chartx.ext.minMaxInRangeByFloat

/**
 * 图表数据管理器（负责数据的收集与最值计算）
 *
 * @author : yangsn
 * @date : 2025/6/20
 */
class ChartDataSet {

    private val _entryList = mutableListOf<ChartEntry>()
    val entryList: List<ChartEntry> get() = _entryList

    var yMin: Float = Float.MAX_VALUE

        private set
    var yMax: Float = Float.MIN_VALUE

        private set

    val yRange: Float
        get() = yMax - yMin

    fun setNewData(newEntries: List<ChartEntry>) {
        _entryList.clear()
        _entryList.addAll(newEntries)
        computeYBounds()
    }

    fun entryCount() = _entryList.size

    fun entryAt(index: Int): ChartEntry? = _entryList.getOrNull(index)

    fun lastIndex() = entryCount() - 1

    private fun computeYBounds() {
        val minMax = _entryList.minMaxInRangeByFloat(0, _entryList.lastIndex) { it.yValue }
        if (minMax == null) {
            yMin = Float.MAX_VALUE
            yMax = Float.MIN_VALUE
        } else {
            yMin = minMax.first.yValue
            yMax = minMax.second.yValue
        }
    }
}
