package wot.core.view.chartx.axis.formatter

import wot.core.view.chartx.axis.model.AxisPosition
import wot.core.view.chartx.axis.model.AxisSide
import wot.core.view.chartx.model.renderer.BaseDataRenderer

/**
 * 图表轴值格式化 [AxisSide.LEFT] or [AxisSide.RIGHT]
 *
 * @author : yangsn
 * @date : 2025/6/16
 */
abstract class YAxisLabelFormatter(dataRenderer: BaseDataRenderer) :
    BaseAxisFormatter(dataRenderer) {

    /**
     * 格式化轴值
     * @param axisSide 轴所在侧
     * @param axisPosition 轴所在位置
     * @param axisValue 轴值
     */
    abstract fun format(axisSide: AxisSide, axisPosition: AxisPosition, axisValue: Float): String
}