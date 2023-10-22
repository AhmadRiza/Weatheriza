package com.weatheriza.core.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager

fun Context.showKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}
