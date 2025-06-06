package wot.core.view.chartx.touch

import android.animation.ValueAnimator
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

/**
 * 手势处理
 *
 * @author : yangsn
 * @date : 2025/6/6
 */
class GestureHandler(private val config: GestureConfig = GestureConfig()) {

    var onGestureListener: OnGestureListener? = null

    private var startX = 0f
    private var startY = 0f
    private var lastX = 0f
    private var lastY = 0f
    private var downTime = 0L

    private var offsetX = 0f
    private var offsetY = 0f

    private var isScaling = false
    private var initialDistance = 0f

    private var lastTapTime = 0L
    private var longPressTriggered = false

    private val velocityTracker = android.view.VelocityTracker.obtain()

    private val longPressRunnable = Runnable {
        if (!isScaling && !longPressTriggered) {
            longPressTriggered = true
            onGestureListener?.onLongPress(startX, startY)
        }
    }

    private var bounceAnimatorX: ValueAnimator? = null
    private var bounceAnimatorY: ValueAnimator? = null

    fun handleTouchEvent(event: MotionEvent, view: View): Boolean {
        velocityTracker.addMovement(event)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                cancelBounceAnimation()
                isScaling = false
                longPressTriggered = false

                startX = event.x
                startY = event.y
                lastX = startX
                lastY = startY
                downTime = System.currentTimeMillis()

                view.postDelayed(longPressRunnable, config.longPressTimeout)
                onGestureListener?.onDown(startX, startY)

                return true
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                if (config.enableScale && event.pointerCount == 2) {
                    isScaling = true
                    initialDistance = calculateDistance(event)
                }
            }

            MotionEvent.ACTION_MOVE -> {
                val currentX = event.x
                val currentY = event.y

                if (isScaling && config.enableScale && event.pointerCount == 2) {
                    val newDistance = calculateDistance(event)
                    val scaleFactor = newDistance / initialDistance
                    val focusX = (event.getX(0) + event.getX(1)) / 2
                    val focusY = (event.getY(0) + event.getY(1)) / 2
                    onGestureListener?.onScale(scaleFactor, focusX, focusY)
                } else {
                    val dxRaw = currentX - lastX
                    val dyRaw = currentY - lastY

                    // 计算阻尼偏移量
                    val (dx, dy) = applyDamping(dxRaw, dyRaw)

                    offsetX += dx
                    offsetY += dy

                    // 限制边界（阻尼后依然可能超出，这里不强制截断，留给回弹）
                    val direction = getDirection(dx, dy)
                    onGestureListener?.onMove(currentX, currentY, dx, dy, direction)

                    // 回调当前阻尼偏移量
                    onGestureListener?.onDragWithDamping(offsetX, offsetY)

                    lastX = currentX
                    lastY = currentY
                }

                return true
            }

            MotionEvent.ACTION_POINTER_UP -> {
                if (event.pointerCount <= 2) {
                    isScaling = false
                }
            }

            MotionEvent.ACTION_UP -> {
                view.removeCallbacks(longPressRunnable)

                val upTime = System.currentTimeMillis()
                val totalDeltaX = event.x - startX

                if (!isScaling && !longPressTriggered) {
                    if (config.enableSwipe && abs(totalDeltaX) > config.swipeThreshold) {
                        if (totalDeltaX > 0) onGestureListener?.onSwipeRight() else onGestureListener?.onSwipeLeft()
                    }

                    // Double Tap
                    if (config.enableDoubleTap && upTime - lastTapTime < config.doubleTapTimeout) {
                        onGestureListener?.onDoubleTap(event.x, event.y)
                        lastTapTime = 0L
                    } else {
                        lastTapTime = upTime
                    }

                    // Fling
                    if (config.enableFling) {
                        velocityTracker.computeCurrentVelocity(1000)
                        val vx = velocityTracker.xVelocity
                        val vy = velocityTracker.yVelocity
                        onGestureListener?.onFling(vx, vy)
                    }
                }

                // 手指抬起时开始回弹动画（如果超出边界）
                startBounceBack()

                onGestureListener?.onUp(event.x, event.y)
                velocityTracker.clear()
                return true
            }
        }
        return false
    }

    private fun applyDamping(dxRaw: Float, dyRaw: Float): Pair<Float, Float> {
        // 如果超出边界，减缓偏移
        var dx = dxRaw
        var dy = dyRaw

        val newOffsetX = offsetX + dx
        val newOffsetY = offsetY + dy

        if (abs(newOffsetX) > config.maxDragX) {
            val exceededX = abs(newOffsetX) - config.maxDragX
            dx *= config.dampingRatio / (1 + exceededX)
        }

        if (abs(newOffsetY) > config.maxDragY) {
            val exceededY = abs(newOffsetY) - config.maxDragY
            dy *= config.dampingRatio / (1 + exceededY)
        }

        return dx to dy
    }

    private fun startBounceBack() {
        // X轴回弹
        val targetX = when {
            offsetX > config.maxDragX -> config.maxDragX
            offsetX < -config.maxDragX -> -config.maxDragX
            else -> offsetX
        }
        if (targetX != offsetX) {
            bounceAnimatorX = ValueAnimator.ofFloat(offsetX, targetX).apply {
                duration = config.bounceBackDuration
                addUpdateListener {
                    offsetX = it.animatedValue as Float
                    onGestureListener?.onBounceBack(offsetX, offsetY)
                }
                start()
            }
        }

        // Y轴回弹
        val targetY = when {
            offsetY > config.maxDragY -> config.maxDragY
            offsetY < -config.maxDragY -> -config.maxDragY
            else -> offsetY
        }
        if (targetY != offsetY) {
            bounceAnimatorY = ValueAnimator.ofFloat(offsetY, targetY).apply {
                duration = config.bounceBackDuration
                addUpdateListener {
                    offsetY = it.animatedValue as Float
                    onGestureListener?.onBounceBack(offsetX, offsetY)
                }
                start()
            }
        }
    }

    private fun cancelBounceAnimation() {
        bounceAnimatorX?.cancel()
        bounceAnimatorY?.cancel()
    }

    private fun calculateDistance(event: MotionEvent): Float {
        if (event.pointerCount >= 2) {
            val dx = event.getX(0) - event.getX(1)
            val dy = event.getY(0) - event.getY(1)
            return kotlin.math.sqrt(dx * dx + dy * dy)
        }
        return 0f
    }

    private fun getDirection(dx: Float, dy: Float): GestureDirection {
        return when {
            abs(dx) > abs(dy) -> {
                if (dx > 0) GestureDirection.RIGHT else GestureDirection.LEFT
            }

            abs(dy) > 0 -> {
                if (dy > 0) GestureDirection.DOWN else GestureDirection.UP
            }

            else -> GestureDirection.NONE
        }
    }
}