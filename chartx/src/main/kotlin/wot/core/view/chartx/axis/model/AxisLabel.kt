package wot.core.view.chartx.axis.model

/**
 * 坐标标签
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
data class AxisLabel(
    val ratio: Float,
    private val manualText: String? = null // 手动设置, 将不会结合 ratio 重新计算值
) {

    var text: String? = null
        get() {
            if (manualText == null) {
                return field
            }
            return manualText // 如果手动设置了, 则返回手动设置的值
        }

    /**
     * 是否需要动态计算 [text] 的值
     */
    fun isNeedCalculateText(): Boolean {
        return manualText == null
    }

    /**
     * x 坐标
     */
    var x: Float = 0F

    /**
     * y 坐标
     */
    var y: Float = 0F
}