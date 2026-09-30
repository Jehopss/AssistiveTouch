package com.hopi.floatmenu

import androidx.dynamicanimation.animation.FloatValueHolder
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce

/**
 * Springs one coordinate of the floating window (x or y) toward a target.
 * Windows aren't Views, so we animate a plain number and hand each new value to onUpdate.
 */
class WindowSpring(
    damping: Float = SpringForce.DAMPING_RATIO_LOW_BOUNCY,
    onUpdate: (Int) -> Unit) {

    private val holder = FloatValueHolder()

    private val animation = SpringAnimation(holder).apply {
        spring = SpringForce().apply {
            stiffness = SpringForce.STIFFNESS_LOW
            dampingRatio = damping
        }
        addUpdateListener { _, value, _ -> onUpdate(value.toInt()) }
    }

    /** Spring from [from] to [to]. */
    fun animate(from: Int, to: Int, velocity: Float = 0f) {
        animation.cancel()
        holder.value = from.toFloat()
        animation.setStartVelocity(velocity)
        animation.animateToFinalPosition(to.toFloat())
    }

    /** Stop right where it is (e.g. the finger grabbed the button again). */
    fun cancel() = animation.cancel()
}