package wot.core.view.chartx.axis.model

import android.graphics.PointF

/**
 * 图表坐标轴线
 *
 * @author : yangsn
 * @date : 2025/6/19
 */
data class AxisLine(val ratio: Float) {

    val startPoint by lazy { PointF() }

    val endPoint by lazy { PointF() }
}