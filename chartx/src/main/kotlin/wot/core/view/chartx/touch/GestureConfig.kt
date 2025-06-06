package wot.core.view.chartx.touch

/**
 * 手势配置
 *
 * @author : yangsn
 * @date : 2025/6/6
 */
data class GestureConfig(
    val enableScale: Boolean = true,
    val enableSwipe: Boolean = true,
    val enableFling: Boolean = true,
    val enableDoubleTap: Boolean = true,
    val enableLongPress: Boolean = true,
    val longPressTimeout: Long = 500L,
    val doubleTapTimeout: Long = 300L,
    val swipeThreshold: Float = 100f,

    // 拖动边界（例如限制x轴拖动范围）
    val maxDragX: Float = 300f,
    val maxDragY: Float = 300f,

    // 阻尼系数，越小阻尼越大（0~1之间）
    val dampingRatio: Float = 0.5f,

    // 回弹动画时长
    val bounceBackDuration: Long = 300L
)