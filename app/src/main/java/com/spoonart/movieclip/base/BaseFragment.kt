package com.spoonart.libs.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.spoonart.movieclip.util.KeyboardUtil


abstract class BaseFragment<T : BasePresenter<*>> : Fragment() {

    protected var presenter: T? = null
    protected var baseActivity: BaseActivity<*>? = null
        get() {
            if (field == null) baseActivity = activity as BaseActivity<*>
            return field!!
        }


    abstract override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        presenter = attachPresenter()
    }

    abstract fun attachPresenter(): T

    protected fun setTitle(title: String) {
        if (isAdded) {
            baseActivity?.setTitle(title)
        }
    }

    override fun onDetach() {
        super.onDetach()
        baseActivity = null
    }

    fun finish() {
        if (isAdded)
            activity!!.finish()
    }

    fun hideKeyboard() {
        KeyboardUtil.hideKeyboard(activity!!)
    }

    fun showToast(message: String) {
        baseActivity?.showToast(message)
    }
}
