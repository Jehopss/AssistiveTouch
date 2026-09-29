package com.hopi.floatmenu

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

/**
 * Owns everything at runtime: the floating button overlay and the global actions.
 * For now it's an empty shell that only proves the service can be enabled.
 */
class FloatMenuService : AccessibilityService() {

    private var floatingButton: FloatingButton? = null

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "Service connected")
        floatingButton = FloatingButton(this)
        floatingButton?.show()
    }

    // The config subscribes to no event types, so this is never called.
    override fun onAccessibilityEvent(event: AccessibilityEvent?) = Unit

    override fun onInterrupt() = Unit

    override fun onDestroy() {
        Log.d(TAG, "Service destroyed")
        floatingButton?.hide()
        floatingButton = null
        super.onDestroy()
    }

    private companion object {
        const val TAG = "FloatMenuService"
    }
}