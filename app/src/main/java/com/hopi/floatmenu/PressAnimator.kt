package com.hopi.floatmenu

import android.view.View
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce

/** Press feedback: shrink a bit on touch, spring back on release. */
class PressAnimator(view: View) {

    // Pegas buat lebar tombol. Posisi awal/akhirnya 1f (ukuran normal).
    private val scaleX = SpringAnimation(view, DynamicAnimation.SCALE_X, 1f).apply {
        spring.stiffness = SpringForce.STIFFNESS_MEDIUM
        spring.dampingRatio = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY
    }

    private val scaleY = SpringAnimation(view, DynamicAnimation.SCALE_Y, 1f).apply {
        spring.stiffness = SpringForce.STIFFNESS_LOW
        spring.dampingRatio = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY
    }

    fun press() {
        scaleX.animateToFinalPosition(0.9f)
        scaleY.animateToFinalPosition(0.9f)
    }

    fun release() {
        scaleX.animateToFinalPosition(1f)
        scaleY.animateToFinalPosition(1f)
    }
}