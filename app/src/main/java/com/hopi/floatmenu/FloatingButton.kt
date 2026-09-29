package com.hopi.floatmenu

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.WindowManager
import android.widget.Toast
import kotlin.math.abs

class FloatingButton(private val context: Context) {
    private val windowManager = context.getSystemService(WindowManager::class.java)
    private var view: View? = null

    fun show() {
        if (view != null) {
            return
        }

        val size = (56 * context.resources.displayMetrics.density).toInt()

        val button = View(context).apply {
            setBackgroundResource(R.drawable.bg_float_button)
            setOnClickListener {
                Toast.makeText(context, "Test Dulu", Toast.LENGTH_SHORT).show()
            }
        }

        val press = PressAnimator(button)

        val params = WindowManager.LayoutParams(
            size,
            size,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 400
        }

        val xSpring = WindowSpring { x ->
            params.x = x
            if (button.isAttachedToWindow) {
                windowManager.updateViewLayout(button, params)
            }
        }

        var startX = 0
        var startY = 0
        var downRawX = 0f
        var downRawY = 0f
        val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
        var dragging = false
        var velocityTracker: VelocityTracker? = null

        button.setOnTouchListener { v, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                velocityTracker?.recycle()
                velocityTracker = VelocityTracker.obtain()
            }
            val screenEvent = MotionEvent.obtain(event)
            screenEvent.setLocation(event.rawX, event.rawY)
            velocityTracker?.addMovement(screenEvent)
            screenEvent.recycle()
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    startX = params.x
                    startY = params.y
                    downRawX = event.rawX
                    downRawY = event.rawY
                    dragging = false
                    press.press()
                    xSpring.cancel()
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = event.rawX - downRawX
                    val dy = event.rawY - downRawY
                    if (!dragging && (abs(dx) > touchSlop || abs(dy) > touchSlop)) {
                        dragging = true
                    }
                    if (dragging) {
                        params.x = startX + dx.toInt()
                        params.y = startY + dy.toInt()
                        windowManager.updateViewLayout(v, params)
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (!dragging) {
                        v.performClick()
                    }

                    press.release()

                    if (dragging) {
                        val screenWidth = windowManager.currentWindowMetrics.bounds.width()
                        val centerX = params.x + size / 2
                        velocityTracker?.computeCurrentVelocity(1000)
                        val vx = velocityTracker?.xVelocity ?: 0f
                        val projectedX = centerX + (vx * 0.2f).toInt()
                        val targetX = if (projectedX < screenWidth / 2) 0 else screenWidth - size
                        xSpring.animate(params.x, targetX, vx)
                    }
                    velocityTracker?.recycle()
                    velocityTracker = nul
                }
                MotionEvent.ACTION_CANCEL -> {
                    press.release()
                    velocityTracker?.recycle()
                    velocityTracker = null
                }
            }
            true
        }

        windowManager.addView(button, params)
        view = button
    }
    fun hide() {
        if (view != null) {
            windowManager.removeView(view)
            view = null
        }
    }
}