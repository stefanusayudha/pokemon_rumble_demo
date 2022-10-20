package com.stefanus_ayudha.pokemonapp.util

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.viewbinding.ViewBinding
import java.lang.reflect.Method

/**
 * This instance should be initiate on view ready
 */
@Deprecated("This class might failure in very small screen size, use DevActivity<*>.observeSoftInputVisibility instead ")
class SoftInputHandler(rootView: View) {

    var onKeyboardVisible: () -> Unit = {}
    var onKeyboardHidden: () -> Unit = {}

    private var maxHeight = 0
    private var keyboardIsHidden = true

    init {
        val r = Rect()
        var frameHeight: Int
        var heightDiff: Int

        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            rootView.getWindowVisibleDisplayFrame(r)

            frameHeight = r.top - r.bottom
            heightDiff = rootView.height - frameHeight

            // setup frame max height
            if (heightDiff > maxHeight) {
                maxHeight = heightDiff
            }

            @Deprecated("The codebase Int.toDp often giving unpredictable value")
            if (heightDiff < maxHeight - 100.dp) {
                if (keyboardIsHidden) {
                    kotlin.runCatching {
                        onKeyboardVisible.invoke()
                    }
                    keyboardIsHidden = false
                }
            } else {
                if (!keyboardIsHidden) {
                    kotlin.runCatching {
                        onKeyboardHidden.invoke()
                    }
                    keyboardIsHidden = true
                }
            }
        }
    }
}

fun ViewBinding.observeSoftInputVisibility(
    onVisible: () -> Unit,
    onHidden: () -> Unit
) {
    root.run {
        viewTreeObserver.addOnGlobalLayoutListener {
            if (root.context.isSystemKeyboardVisible()) {
                onVisible.invoke()
            } else {
                onHidden.invoke()
            }
        }
    }
}

private fun Context.isSystemKeyboardVisible(): Boolean {
    return try {
        val manager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val windowHeightMethod: Method =
            InputMethodManager::class.java.getMethod("getInputMethodWindowVisibleHeight")
        val height = windowHeightMethod.invoke(manager) as Int
        height > 0
    } catch (e: Exception) {
        false
    }
}