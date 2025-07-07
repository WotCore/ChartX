package wot.core.view.chartx.touch

import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import kotlin.math.abs
import kotlin.math.hypot

/**
 * 手势处理器
 * 每次只处理一种手势（单指触摸、缩放、长按、双击）
 */
class GestureHandler {

    var onTouchListener: OnTouchListener? = null
    var onScaleListener: OnScaleListener? = null
    var onDoubleTapListener: OnDoubleTapListener? = null
    var onLongPressListener: OnLongPressListener? = null

    private var lastX = 0f
    private var lastY = 0f

    private var scaleFactor = 1.5F // 缩放因子, 增加缩放跨度
    private var pointerId1 = -1
    private var pointerId2 = -1
    private var lastPointDist = 0f // 当前两个点的距离

    private val doubleTapTimeout = 300L
    private var lastTapTime = 0L
    private var lastTapX = 0f
    private var lastTapY = 0f

    private val longPressTimeout = 500L
    private val handler = Handler(Looper.getMainLooper())
    private var isLongPressTriggered = false

    private enum class GestureState {
        NONE, TOUCH, SCALE, LONG_PRESS
    }

    private var gestureState = GestureState.NONE

    private val longPressRunnable = Runnable {
        isLongPressTriggered = true
        gestureState = GestureState.LONG_PRESS
        onLongPressListener?.onLongPress(lastX, lastY)
    }

    fun handleTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                gestureState = GestureState.TOUCH
                isLongPressTriggered = false

                lastX = event.x
                lastY = event.y
                pointerId1 = event.getPointerId(0)

                handler.postDelayed(longPressRunnable, longPressTimeout)

                onTouchListener?.onDown(lastX, lastY)
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount == 2) {
                    gestureState = GestureState.SCALE
                    isLongPressTriggered = false
                    handler.removeCallbacks(longPressRunnable)

                    pointerId1 = event.getPointerId(0)
                    pointerId2 = event.getPointerId(1)

                    val x1 = event.getX(0)
                    val y1 = event.getY(0)
                    val x2 = event.getX(1)
                    val y2 = event.getY(1)
                    lastPointDist = distance(x1, y1, x2, y2)
                }
            }

            MotionEvent.ACTION_MOVE -> handleMove(event)

            MotionEvent.ACTION_POINTER_UP -> {
                if (gestureState == GestureState.SCALE) {
                    val pointerId = event.getPointerId(event.actionIndex)
                    if (pointerId == pointerId1 || pointerId == pointerId2) {
                        gestureState = GestureState.NONE
                    }
                }
            }

            MotionEvent.ACTION_UP -> {
                handler.removeCallbacks(longPressRunnable)

                val x = event.x
                val y = event.y

                onTouchListener?.onUp(x, y)

                if (gestureState != GestureState.SCALE && !isLongPressTriggered) {
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
                }

                gestureState = GestureState.NONE
                isLongPressTriggered = false
            }

            MotionEvent.ACTION_CANCEL -> {
                handler.removeCallbacks(longPressRunnable)
                gestureState = GestureState.NONE
                isLongPressTriggered = false
            }
        }

        return true
    }

    private fun handleMove(event: MotionEvent) {
        when (gestureState) {
            GestureState.SCALE -> {
                if (event.pointerCount >= 2) {
                    zoom(event)
                }
            }

            GestureState.TOUCH -> {
                val x = event.x
                val y = event.y
                val deltaX = x - lastX
                val deltaY = y - lastY

                if (!isLongPressTriggered) {
                    handler.removeCallbacks(longPressRunnable)
                }

                onTouchListener?.onMove(x, y, deltaX, deltaY)

                lastX = x
                lastY = y
            }

            else -> {
                // 忽略 LONG_PRESS 和 NONE 状态下的 move
            }
        }
    }

    private fun zoom(event: MotionEvent) {
        val index1 = event.findPointerIndex(pointerId1)
        val index2 = event.findPointerIndex(pointerId2)
        if (index1 == -1 || index2 == -1) return

        val x1 = event.getX(index1)
        val y1 = event.getY(index1)
        val x2 = event.getX(index2)
        val y2 = event.getY(index2)
        val pointDist = distance(x1, y1, x2, y2)

        val scale = pointDist / lastPointDist
        if (scale > 1f || scale < 1f) {
            onScaleListener?.onScale(scaleFactor * (scale - 1), x1, y1, x2, y2)
        }

        lastPointDist = pointDist
    }

    /**
     * 计算两点之间的距离
     */
    private fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return hypot(x2 - x1, y2 - y1)
    }
}
