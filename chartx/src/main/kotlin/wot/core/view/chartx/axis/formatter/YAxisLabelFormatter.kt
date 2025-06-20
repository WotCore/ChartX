package wot.core.view.chartx.axis.formatter

import wot.core.view.chartx.axis.model.AxisPosition
import wot.core.view.chartx.axis.model.AxisSide

/**
 * 图表轴值格式化 [AxisSide.LEFT] or [AxisSide.RIGHT]
 *
 * @author : yangsn
 * @date : 2025/6/16
 */
interface YAxisLabelFormatter : IAxisLabelFormatter {

    /**
     * 格式化轴值
     * @param axisSide 轴所在侧
     * @param axisPosition 轴所在位置
     * @param axisValue 轴值
     */
    fun format(axisSide: AxisSide, axisPosition: AxisPosition, axisValue: Float): String
}