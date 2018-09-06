package com.spoonart.movieclip.extension

import android.accounts.NetworkErrorException
import android.support.v4.widget.SwipeRefreshLayout
import com.spoonart.libs.base.BasePresenter
import com.spoonart.libs.base.IBaseView
import com.spoonart.movieclip.R
import com.spoonart.movieclip.network.ApiConfig
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun SwipeRefreshLayout.setRefresh(enable: Boolean) {
    this.post {
        isRefreshing = enable
    }
}

fun wrapPathUrl(path: String): String {
    if (!path.contains(ApiConfig.IMAGE_BASE_URL))
        return "${ApiConfig.IMAGE_BASE_URL}$path"

    return path
}

fun <T : IBaseView, O> BasePresenter<T>.tryCall(observable: Observable<O>, isWrapThread: Boolean = true): Observable<O> {
    if (!this.isNetworkAvailable()) {
        this.iView.taskDidFinish()
        return Observable.error(NetworkErrorException(context!!.getString(R.string.text_error_no_internet_connection)))
    }

    if (isWrapThread)
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    return observable
}

fun BasePresenter<*>.getApiKey(): String {
    return ApiConfig.API_KEY
}