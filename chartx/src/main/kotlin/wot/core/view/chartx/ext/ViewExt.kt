package wot.core.view.chartx.ext

import android.view.View

/**
 * View 扩展函数
 *
 * @author : yangsn
 * @date : 2025/6/30
 */

/**
 * 视图还未测量完成，或者宽高为0，视为无效
 */
fun View.isSizeInvalid(): Boolean {
    return measuredWidth <= 0 || measuredHeight <= 0
}
