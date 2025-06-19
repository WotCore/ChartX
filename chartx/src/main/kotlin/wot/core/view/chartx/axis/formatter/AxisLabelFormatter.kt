package wot.core.view.chartx.axis.formatter

import wot.core.view.chartx.axis.model.AxisPosition
import wot.core.view.chartx.axis.model.AxisSide

/**
 * 图表轴值格式化
 *
 * @author : yangsn
 * @date : 2025/6/16
 */
interface AxisLabelFormatter {

    /**
     * 格式化轴值
     * @param side 轴所在侧
     * @param axisPosition 轴所在位置
     * @param value 轴值
     */
    fun format(side: AxisSide, axisPosition: AxisPosition, value: Float): String
}