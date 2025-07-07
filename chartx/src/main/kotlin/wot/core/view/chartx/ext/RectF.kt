package wot.core.view.chartx.ext

import android.graphics.RectF

/**
 * RectF 扩展函数
 *
 * @author : yangsn
 * @date : 2025/6/30
 */

/**
 * 是否无效
 */
fun RectF.isInvalid(): Boolean {
    return isEmpty || listOf(left, top, right, bottom).any { it.isNaN() || it.isInfinite() }
}
