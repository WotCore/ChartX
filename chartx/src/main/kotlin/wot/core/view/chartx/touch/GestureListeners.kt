package wot.core.view.chartx.touch

/**
 * 手势监听类
 *
 * @author : yangsn
 * @date : 2025/6/6
 */

/**
 * 触摸事件
 */
interface OnTouchListener {

    /**
     * 用户按下手指（单指）时触发。
     * 通常用于记录初始位置、停止动画等。
     *
     * @param x 当前触点的 X 坐标
     * @param y 当前触点的 Y 坐标
     */
    fun onDown(x: Float, y: Float) {}

    /**
     * 手指移动时持续回调（仅单指拖动状态下）。
     * 会返回偏移量 deltaX 以及当前判断的方向。
     *
     * @param x 当前触点的 X 坐标
     * @param y 当前触点的 Y 坐标
     * @param deltaX 与上一次移动的 X 偏移量（正为右，负为左）
     * @param deltaY 与上一次移动的 Y 偏移量
     */
    fun onMove(x: Float, y: Float, deltaX: Float, deltaY: Float) {}

    /**
     * 手指抬起时触发（仅在非缩放状态下）。
     * 可用于做释放后回弹、判断最终是否需要触发动作。
     *
     * @param x 当前触点的 X 坐标
     * @param y 当前触点的 Y 坐标
     */
    fun onUp(x: Float, y: Float) {}
}

/**
 * 双指缩放
 */
interface OnScaleListener {

    /**
     * 双指缩放时持续触发，返回缩放比例。
     *
     * @param scale 当前这次手势缩放相对于上一次的“缩放变化百分比”
     * @param x1 第一个触点的 X 坐标
     * @param y1 第一个触点的 Y 坐标
     * @param x2 第二个触点的 X 坐标
     * @param y2 第二个触点的 Y 坐标
     */
    fun onScale(scale: Float, x1: Float, y1: Float, x2: Float, y2: Float) {}
}

/**
 * 双击事件
 */
interface OnDoubleTapListener {

    /**
     * 双击事件回调。
     * 当检测到快速连续两次点击时触发，通常用于触发放大、刷新等操作。
     *
     * @param x 双击时触点的 X 坐标
     * @param y 双击时触点的 Y 坐标
     */
    fun onDoubleTap(x: Float, y: Float) {}
}

/**
 * 长按事件
 */
interface OnLongPressListener {

    /**
     * 长按事件回调。
     * 当用户手指按住屏幕超过一定时间（如500ms）时触发，常用于弹出菜单、开始拖动等交互。
     *
     * @param x 长按时触点的 X 坐标
     * @param y 长按时触点的 Y 坐标
     */
    fun onLongPress(x: Float, y: Float) {}
}