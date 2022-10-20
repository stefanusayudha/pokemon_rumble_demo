package com.stefanus_ayudha.pokemonapp.util

import android.content.res.Resources

// View Properties
var density: Float = Resources.getSystem().displayMetrics.density

val Int.dp: Int
    get() = (density * this).toInt()