package wot.core.view.chartx.touch

/**
 * 功能说明
 *
 * @author : yangsn
 * @date : 2025/6/6
 */
interface OnGestureListener {

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
     * @param direction 当前移动的方向（左、右、无）
     */
    fun onMove(x: Float, y: Float, dx: Float, dy: Float, direction: GestureDirection) {}

    /**
     * 手指抬起时触发（仅在非缩放状态下）。
     * 可用于做释放后回弹、判断最终是否需要触发动作。
     *
     * @param x 当前触点的 X 坐标
     * @param y 当前触点的 Y 坐标
     */
    fun onUp(x: Float, y: Float) {}

    /**
     * 判断为向左滑动（快速滑动且距离大于阈值）时触发。
     * 用于触发“翻页”、“删除”等操作。
     */
    fun onSwipeLeft() {}

    /**
     * 判断为向右滑动（快速滑动且距离大于阈值）时触发。
     * 用于触发“返回”、“点赞”等操作。
     */
    fun onSwipeRight() {}

    /**
     * 双指缩放时持续触发，返回缩放比例。
     * 可用于缩放图片、地图、视图等。
     *
     * @param scaleFactor 当前缩放比例（>1 放大，<1 缩小）
     * @param focusX 两指中点的 X 坐标（用于以该点为中心缩放）
     * @param focusY 两指中点的 Y 坐标
     */
    fun onScale(scaleFactor: Float, focusX: Float, focusY: Float) {}

    /**
     * 双击事件回调。
     * 当检测到快速连续两次点击时触发，通常用于触发放大、刷新等操作。
     *
     * @param x 双击时触点的 X 坐标
     * @param y 双击时触点的 Y 坐标
     */
    fun onDoubleTap(x: Float, y: Float) {}

    /**
     * 长按事件回调。
     * 当用户手指按住屏幕超过一定时间（如500ms）时触发，常用于弹出菜单、开始拖动等交互。
     *
     * @param x 长按时触点的 X 坐标
     * @param y 长按时触点的 Y 坐标
     */
    fun onLongPress(x: Float, y: Float) {}

    /**
     * 快速滑动（甩动）事件回调。
     * 当用户快速滑动屏幕，手指离开时触发，通常用于实现惯性滚动、快速翻页等效果。
     *
     * @param velocityX X 方向上的滑动速度（像素/秒，正为向右，负为向左）
     * @param velocityY Y 方向上的滑动速度（像素/秒，正为向下，负为向上）
     */
    fun onFling(velocityX: Float, velocityY: Float) {}

    /**
     * 拖动时，当前阻尼后的偏移量，供外部更新UI用
     */
    fun onDragWithDamping(offsetX: Float, offsetY: Float) {}
    /**
     * 回弹动画更新时，回调当前位置
     */
    fun onBounceBack(progressX: Float, progressY: Float) {}
}