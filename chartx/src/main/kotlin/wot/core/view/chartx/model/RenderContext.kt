package wot.core.view.chartx.model

import android.graphics.Matrix
import android.graphics.RectF
import wot.core.view.chartx.ext.minMaxInRangeByFloat
import wot.core.view.chartx.renderer.BaseDataRenderer

/**
 * 渲染器上下文
 *
 * @author : yangsn
 * @date : 2025/6/2
 */
class RenderContext(var renderer: BaseDataRenderer) {

    var yMinValue: Float = Float.MAX_VALUE
        private set
    var yMaxValue: Float = Float.MIN_VALUE
        private set
    var yValueRange: Float = 0F
        private set

    /**
     * 数据值映射成屏幕像素值的矩阵
     */
    private val mValueToPxMatrix by lazy { Matrix() }
    private val mPixelToValueMatrixBuffer by lazy { Matrix() }

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

    /**
     * 值映射成像素的矩阵的准备工作
     *
     */
    fun prepareValueToPxMatrix(contentRect: RectF, startIndex: Int, pointWidth: Float) {
        var scaleX: Float = pointWidth
        var scaleY: Float = contentRect.height() / yValueRange

        if (java.lang.Float.isInfinite(scaleX) || java.lang.Float.isNaN(scaleX)) {
            scaleX = 0f
        }
        if (java.lang.Float.isInfinite(scaleY) || java.lang.Float.isNaN(scaleX)) {
            scaleY = 0f
        }
        mValueToPxMatrix.reset()
        mValueToPxMatrix.postTranslate(-startIndex.toFloat(), -yMinValue)
        mValueToPxMatrix.postScale(scaleX, -scaleY)
        mValueToPxMatrix.postTranslate(contentRect.left, contentRect.bottom)
    }

    /**
     * 值数据映射成像素数据
     *
     * @param pointValues 值数据
     */
    fun mapPointValuesToPixel(pointValues: FloatArray) {
        mValueToPxMatrix.mapPoints(pointValues)
    }

    /**
     * 坐标像素映射成数据值
     *
     * @param pixels
     */
    fun mapPointPixelsToValue(pixels: FloatArray) {
        mPixelToValueMatrixBuffer.reset()

        // 逆矩阵得到原始值
        mValueToPxMatrix.invert(mPixelToValueMatrixBuffer)
        mPixelToValueMatrixBuffer.mapPoints(pixels)
    }
}