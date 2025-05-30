package wot.core.view.chartx.axis.model

/**
 * 坐标标签
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
data class AxisLabel(val ratio: Float, var text: String? = null) {

    /**
     * x 坐标
     */
    var x: Float = 0F

    /**
     * y 坐标
     */
    var y: Float = 0F
}