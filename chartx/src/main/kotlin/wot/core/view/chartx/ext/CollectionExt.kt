package wot.core.view.chartx.ext

/**
 * 集合扩展类
 *
 * @author : yangsn
 * @date : 2025/6/2
 */
/**
 * 是有效索引
 */
fun <T> List<T>?.isValidIndex(index: Int): Boolean {
    return this != null && index in indices
}

/**
 * 是无效索引
 */
fun <T> List<T>?.isInvalidIndex(index: Int): Boolean {
    return this == null || index !in indices
}

/**
 * 指定区间 [startIndex] 到 [endIndex] 中查找：
 * 1. 最小值（根据 selector 映射值）
 * 2. 最大值（根据 selector 映射值）
 *
 * @param selector 提取用于比较的 Float 值
 * @return Pair(最小值, 最大值)
 */
fun <T> List<T>.minMaxInRange(
    startIndex: Int,
    endIndex: Int,
    selector: (T) -> Float
): Pair<T, T>? {
    if (startIndex > endIndex || startIndex !in indices || endIndex !in indices) return null

    // 单元素优化
    if (startIndex == endIndex) {
        val item = this[startIndex]
        return Pair(item, item)
    }

    val range = startIndex..endIndex
    var minItem = this[startIndex]
    var maxItem = minItem

    var minValue = selector(minItem)
    var maxValue = minValue

    for (i in range.drop(1)) {
        val item = this[i]
        val value = selector(item)

        if (value < minValue) {
            minValue = value
            minItem = item
        }
        if (value > maxValue) {
            maxValue = value
            maxItem = item
        }
    }

    return Pair(minItem, maxItem)
}