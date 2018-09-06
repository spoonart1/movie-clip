package com.spoonart.libs.base

import android.content.Context
import android.support.v7.widget.LinearLayoutManager

/**
 * Created by Lafran on 11/7/17.
 */

class BaseLinearLayoutManager(context: Context) : LinearLayoutManager(context){
    var isScrollenabled = true

    override fun canScrollVertically(): Boolean {
        return isScrollenabled && super.canScrollVertically()
    }
}
