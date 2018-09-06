package com.spoonart.libs.base

import android.support.annotation.StringRes

/**
 * Created by Lafran on 7/25/17.
 */

interface IBaseView {
    fun onViewInited()
    fun taskDidError(message: String) {}
    fun taskDidError(@StringRes resString: Int) {}

    //optionals function
    fun taskDidBegin() {}

    fun taskDidFinish() {}
    fun taskSessionDidInvalid() {}
}
