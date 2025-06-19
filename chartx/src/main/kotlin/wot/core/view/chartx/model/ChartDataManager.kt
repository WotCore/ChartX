package wot.core.view.chartx.model

import android.graphics.Matrix
import android.graphics.RectF
import wot.core.view.chartx.ext.minMaxInRangeByFloat

/**
 * 数据渲染器的 数据管理类
 *
 * @author : yangsn
 * @date : 2025/6/2
 */
class ChartDataManager {

    var yMinValue: Float = Float.MAX_VALUE
        private set
    var yMaxValue: Float = Float.MIN_VALUE
        private set
    var yValueRange: Float = 0F
        private set

    /**
     * 数据值映射成屏幕像素值的矩阵
     */
    private val valueToPxMatrix by lazy { Matrix() }
    private val pixelToValueMatrixBuffer by lazy { Matrix() }

    // ========== 数据相关 ==========

    /**
     * 面板绑定数据
     */
    val entryList by lazy { mutableListOf<ChartEntry>() }

    /**
     * 设置新数据
     */
    fun setNewData(entryList: List<ChartEntry>) {
        this.entryList.clear()
        this.entryList.addAll(entryList)

        calcMinMaxValues()
    }

    /**
     * 最大索引
     */
    fun entryMaxIndex() = entrySize() - 1

    /**
     * entry size
     */
    fun entrySize() = entryList.size

    /**
     * 计算最大最小值
     */
    private fun calcMinMaxValues() {
        val minMaxPair =
            entryList.minMaxInRangeByFloat(0, entryList.size - 1, { it.yValue })

        if (minMaxPair == null) {
            yMinValue = Float.MAX_VALUE
            yMaxValue = Float.MIN_VALUE
        } else {
            yMinValue = minMaxPair.first.yValue
            yMaxValue = minMaxPair.second.yValue
        }
        yValueRange = yMaxValue - yMinValue
    }

    // ========== 值映射相关 ==========

    /**
     * 值映射成像素的矩阵的准备工作
     */
    fun buildValueToPixelMatrix(contentRect: RectF, startIndex: Int, pointWidth: Float) {
        var scaleX: Float = pointWidth
        var scaleY: Float = contentRect.height() / yValueRange

        if (java.lang.Float.isInfinite(scaleX) || java.lang.Float.isNaN(scaleX)) {
            scaleX = 0f
        }
        if (java.lang.Float.isInfinite(scaleY) || java.lang.Float.isNaN(scaleX)) {
            scaleY = 0f
        }
        valueToPxMatrix.reset()
        valueToPxMatrix.postTranslate(-startIndex.toFloat(), -yMinValue)
        valueToPxMatrix.postScale(scaleX, -scaleY)
        valueToPxMatrix.postTranslate(contentRect.left, contentRect.bottom)
    }

    /**
     * 值数据映射成像素数据
     *
     * @param pointValues 值数据
     */
    fun mapDataToPixels(pointValues: FloatArray) {
        valueToPxMatrix.mapPoints(pointValues)
    }

    /**
     * 坐标像素映射成数据值
     */
    fun mapPixelsToData(pixels: FloatArray) {
        pixelToValueMatrixBuffer.reset()

        // 逆矩阵得到原始值
        valueToPxMatrix.invert(pixelToValueMatrixBuffer)
        pixelToValueMatrixBuffer.mapPoints(pixels)
    }
}