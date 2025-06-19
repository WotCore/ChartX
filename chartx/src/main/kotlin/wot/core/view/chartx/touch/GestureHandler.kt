package wot.core.view.chartx.touch

import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import kotlin.math.abs
import kotlin.math.hypot

/**
 * 手势处理
 *
 * @author : yangsn
 * @date : 2025/6/6
 */
class GestureHandler {

    var onTouchListener: OnTouchListener? = null
    var onScaleListener: OnScaleListener? = null
    var onDoubleTapListener: OnDoubleTapListener? = null
    var onLongPressListener: OnLongPressListener? = null

    /**
     * 最小缩放比例
     */
    var minScale = 0.5f

    /**
     * 最大缩放比例
     */
    var maxScale = 3.0f

    private var lastX = 0f
    private var lastY = 0f

    private var isScaling = false

    private var pointerId1 = -1
    private var pointerId2 = -1
    private var initialDistance = 0f

    private val doubleTapTimeout = 300L
    private var lastTapTime = 0L
    private var lastTapX = 0f
    private var lastTapY = 0f

    private val longPressTimeout = 500L
    private val handler = Handler(Looper.getMainLooper())
    private var isLongPressTriggered = false

    private val longPressRunnable = Runnable {
        isLongPressTriggered = true
        onLongPressListener?.onLongPress(lastX, lastY)
    }

    fun handleTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                isLongPressTriggered = false
                handler.postDelayed(longPressRunnable, longPressTimeout)

                lastX = event.x
                lastY = event.y
                pointerId1 = event.getPointerId(0)

                onTouchListener?.onDown(lastX, lastY)
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount == 2) {
                    handler.removeCallbacks(longPressRunnable)
                    isLongPressTriggered = false
                    isScaling = true
                    pointerId1 = event.getPointerId(0)
                    pointerId2 = event.getPointerId(1)

                    val x1 = event.getX(0)
                    val y1 = event.getY(0)
                    val x2 = event.getX(1)
                    val y2 = event.getY(1)
                    initialDistance = distance(x1, y1, x2, y2)
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (isScaling && event.pointerCount >= 2) {
                    val index1 = event.findPointerIndex(pointerId1)
                    val index2 = event.findPointerIndex(pointerId2)
                    if (index1 != -1 && index2 != -1) {
                        val x1 = event.getX(index1)
                        val y1 = event.getY(index1)
                        val x2 = event.getX(index2)
                        val y2 = event.getY(index2)
                        val newDist = distance(x1, y1, x2, y2)
                        if (initialDistance > 0) {
                            var scaleFactor = newDist / initialDistance
                            if (scaleFactor < minScale) scaleFactor = minScale
                            if (scaleFactor > maxScale) scaleFactor = maxScale

                            val focusX = (x1 + x2) / 2
                            val focusY = (y1 + y2) / 2
                            onScaleListener?.onScale(scaleFactor, focusX, focusY)
                        }
                    }
                } else if (!isScaling) {
                    val x = event.x
                    val y = event.y
                    val deltaX = x - lastX
                    val deltaY = y - lastY

                    if (!isLongPressTriggered) {
                        // 不再检测最小移动距离，直接移除长按回调
                        handler.removeCallbacks(longPressRunnable)
                    }

                    onTouchListener?.onMove(x, y, deltaX, deltaY)

                    lastX = x
                    lastY = y
                }
            }

            MotionEvent.ACTION_POINTER_UP -> {
                if (isScaling) {
                    val pointerId = event.getPointerId(event.actionIndex)
                    if (pointerId == pointerId1 || pointerId == pointerId2) {
                        isScaling = false
                    }
                }
            }

            MotionEvent.ACTION_UP -> {
                handler.removeCallbacks(longPressRunnable)

                val x = event.x
                val y = event.y

                onTouchListener?.onUp(x, y)

                val currentTime = System.currentTimeMillis()
                if (currentTime - lastTapTime <= doubleTapTimeout) {
                    val dx = abs(x - lastTapX)
                    val dy = abs(y - lastTapY)
                    if (dx < 50 && dy < 50) {
                        onDoubleTapListener?.onDoubleTap(x, y)
                    }
                }
                lastTapTime = currentTime
                lastTapX = x
                lastTapY = y

                isLongPressTriggered = false
            }

            MotionEvent.ACTION_CANCEL -> {
                handler.removeCallbacks(longPressRunnable)
                isScaling = false
                isLongPressTriggered = false
            }
        }
        return true
    }

    private fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return hypot(x2 - x1, y2 - y1)
    }
}