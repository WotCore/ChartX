package wot.core.view.chartx.utils

/**
 * 数学计算工具类
 *
 * @author : yangsn
 * @date : 2025/7/4
 */
object MathUtils {

    /**
     * 扩大范围值
     *
     * @param min 范围最小值
     * @param max 范围最大值
     * @param scale 放大比例，默认1.2倍
     * @return 放大后的范围 Pair(newMin, newMax)
     * @throws IllegalArgumentException 如果 min > max 或 scale < 0
     */
    @JvmStatic
    fun expandRange(min: Float, max: Float, scale: Float): Pair<Float, Float> {
        require(min <= max) { "min should not be greater than max" }
        require(scale >= 0f) { "scale must be non-negative" }

        val center = (max + min) * 0.5f
        val halfRange = (max - min) * 0.5f
        val newHalfRange = halfRange * scale

        return (center - newHalfRange) to (center + newHalfRange)
    }
}