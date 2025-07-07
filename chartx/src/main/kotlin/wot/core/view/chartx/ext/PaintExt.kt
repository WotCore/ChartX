package wot.core.view.chartx.ext

import android.graphics.Paint
import android.graphics.RectF

/**
 * Paint 扩展函数
 *
 * @author : yangsn
 * @date : 2025/5/29
 */
/**
 * 计算绘制文字时，为了使其垂直居中，应该使用的基线 Y 坐标
 */
fun Paint.centerY(rectF: RectF): Float {
    val fm = fontMetrics
    return rectF.centerY() - (fm.ascent + fm.descent) / 2
}

/**
 * 计算文字宽度（使用 measureText）
 */
fun Paint.textWidth(text: String?): Float {
    text ?: return 0F
    return measureText(text)
}