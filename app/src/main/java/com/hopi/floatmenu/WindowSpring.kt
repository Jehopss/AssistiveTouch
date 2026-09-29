package com.hopi.floatmenu

import androidx.dynamicanimation.animation.FloatValueHolder
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce

/**
 * Springs one coordinate of the floating window (x or y) toward a target.
 * Windows aren't Views, so we animate a plain number and hand each new value to onUpdate.
 */
class WindowSpring(onUpdate: (Int) -> Unit) {
    //             └─ "fungsi titipan": dipanggil tiap angkanya berubah

    // Angka biasa yang dianimasikan
    private val holder = FloatValueHolder()

    private val animation = SpringAnimation(holder).apply {
        spring = SpringForce().apply {
            stiffness = SpringForce.STIFFNESS_LOW            // tarikan agak santai
            dampingRatio = SpringForce.DAMPING_RATIO_LOW_BOUNCY  // membal dikit
        }
        // Tiap frame: kasih angka terbaru ke fungsi titipan
        addUpdateListener { _, value, _ -> onUpdate(value.toInt()) }
    }

    /** Spring from [from] to [to]. */
    fun animate(from: Int, to: Int) {
        animation.cancel()
        holder.value = from.toFloat()
        animation.animateToFinalPosition(to.toFloat())
    }

    /** Stop right where it is (e.g. the finger grabbed the button again). */
    fun cancel() = animation.cancel()
}