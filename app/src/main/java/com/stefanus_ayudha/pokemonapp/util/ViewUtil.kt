package com.stefanus_ayudha.pokemonapp.util

import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation

private val clickAnimation: (View) -> ScaleAnimation =
    {
        ScaleAnimation(
            1f,
            .9f,
            1f,
            .9f,
            it.width/2f,
            it.height/2f
        ).apply {
            duration = 300
        }
    }

fun View.animateClick(callback: () -> Unit = {}) {
    val animation = clickAnimation(this).apply {
        setAnimationListener(
            object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    callback.invoke()
                }

                override fun onAnimationRepeat(animation: Animation?) {

                }

            }
        )
    }
    startAnimation(animation)
}