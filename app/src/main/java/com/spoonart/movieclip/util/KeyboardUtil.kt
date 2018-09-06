package com.spoonart.movieclip.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by mzennis on 9/13/16.
 */
object KeyboardUtil {

    fun hideKeyboard(activity: Activity) {
        try {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val contentView = activity.findViewById<View>(android.R.id.content)
            imm.hideSoftInputFromWindow(contentView.windowToken, 0)
        } catch (ignored: Exception) {
        }

    }

    fun showKeyboard(edittext: View) {
        try {
            val imm = edittext.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(edittext, 0)
        } catch (ignored: Exception) {
        }

    }
}
