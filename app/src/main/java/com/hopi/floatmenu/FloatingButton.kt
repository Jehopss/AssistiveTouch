package com.hopi.floatmenu

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.MotionEvent
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
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 400
        }

        var startX = 0
        var startY = 0
        var downRawX = 0f
        var downRawY = 0f
        val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
        var dragging = false

        button.setOnTouchListener { v, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    startX = params.x
                    startY = params.y
                    downRawX = event.rawX
                    downRawY = event.rawY
                    dragging = false
                    press.press()
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
                }
                MotionEvent.ACTION_CANCEL -> {
                    press.release()
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