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
 * 指定区间[startIndex]-[endIndex]找最大值和最小值
 */
fun <T> List<T>.minMaxInRange(
    startIndex: Int, endIndex: Int, comparator: Comparator<T>
): Pair<T, T>? {
    if (startIndex < 0 || endIndex >= this.size || startIndex > endIndex) return null
    val subList = this.subList(startIndex, endIndex + 1)
    var min = subList[0]
    var max = subList[0]
    for (item in subList) {
        if (comparator.compare(item, min) < 0) min = item
        if (comparator.compare(item, max) > 0) max = item
    }
    return min to max
}

/**
 * 指定区间[startIndex]-[endIndex]找最大值和最小值
 */
fun <T> List<T>.minMaxInRangeByFloat(
    startIndex: Int,
    endIndex: Int,
    selector: (T) -> Float
): Pair<T, T>? {
    if (startIndex < 0 || endIndex >= this.size || startIndex > endIndex) return null

    var min = this[startIndex]
    var max = this[startIndex]
    var minValue = selector(min)
    var maxValue = selector(max)

    for (i in (startIndex + 1)..endIndex) {
        val item = this[i]
        val value = selector(item)
        if (value < minValue) {
            minValue = value
            min = item
        }
        if (value > maxValue) {
            maxValue = value
            max = item
        }
    }
    return min to max
}

/**
 * 在指定区间 [startIndex] 到 [endIndex] 中，
 * 分别通过 [minSelector] 和 [maxSelector] 查找最小值与最大值对应的元素。
 */
fun <T> List<T>.minMaxInRangeByFloat(
    startIndex: Int,
    endIndex: Int,
    minSelector: (T) -> Float,
    maxSelector: (T) -> Float
): Pair<T, T>? {
    if (startIndex < 0 || endIndex >= this.size || startIndex > endIndex) return null

    var min = this[startIndex]
    var max = this[startIndex]
    var minValue = minSelector(min)
    var maxValue = maxSelector(max)

    for (i in (startIndex + 1)..endIndex) {
        val item = this[i]
        val minV = minSelector(item)
        val maxV = maxSelector(item)

        if (minV < minValue) {
            minValue = minV
            min = item
        }
        if (maxV > maxValue) {
            maxValue = maxV
            max = item
        }
    }
    return min to max
}