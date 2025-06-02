package wot.core.view.chartx.model

/**
 * 功能说明
 *
 * @author : yangsn
 * @date : 2025/6/3
 */
class Viewport {

    /**
     * 点的最大宽度
     */
    var pointMaxWidth: Float = 20F

    /**
     * 最大可见点数
     */
    var maxVisiblePoints: Int = 0
    var maxIndex: Int = 0 // 最大索引
        private set
    var startIndex: Int = 0 // 起始索引
        private set
    var endIndex: Int = -1 // 结束索引
        private set

    /**
     * 图表内容宽
     */
    private var contentWidth: Float = 0F

    fun setContentWidth(contentWidth: Float) {
        this.contentWidth = contentWidth
        calcMaxVisiblePoints()
        checkIndex()
    }

    /**
     * 更新最大索引
     */
    fun updateMaxIndex(max: Int) {
        // 先计算结束索引
        endIndex = if (endIndex == -1) {
            // 未赋值, 直接赋值
            max
        } else {
            // 已赋值, 则计算出新的结束索引
            endIndex - maxIndex + max
        }

        maxIndex = maxIndex.coerceAtLeast(max)

        // 计算开始索引
        calcStartIndex()
    }

    /**
     * 计算最大可见点数
     */
    private fun calcMaxVisiblePoints() {
        maxVisiblePoints = (contentWidth / pointMaxWidth).toInt()
    }

    /**
     * 检查索引
     */
    private fun checkIndex() {
        if (startIndex == 0) {
            calcEndIndex()
        } else {
            calcStartIndex()
        }
    }

    /**
     * 计算开始索引
     */
    private fun calcStartIndex() {
        startIndex = maxIndex - maxVisiblePoints + 1
        // 限制边界
        if (startIndex < 0) {
            startIndex = 0
        } else if (startIndex > maxIndex) {
            startIndex = maxIndex
        }
    }

    /**
     * 计算结束索引
     */
    private fun calcEndIndex() {
        endIndex = startIndex + maxVisiblePoints - 1
        // 限制边界
        if (endIndex < 0) {
            endIndex = 0
        } else if (endIndex > maxIndex) {
            endIndex = maxIndex
        }
    }
}