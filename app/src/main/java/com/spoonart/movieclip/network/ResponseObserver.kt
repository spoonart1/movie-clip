package com.spoonart.movieclip.network

import android.support.annotation.CallSuper
import com.google.gson.Gson
import com.spoonart.libs.base.IBaseView
import com.spoonart.movieclip.BuildConfig
import com.spoonart.movieclip.model.ResponseBody
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import retrofit2.HttpException

open class ResponseObserver<T>(val bag: CompositeDisposable, var view: IBaseView) : Observer<T> {

    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {
        bag.add(d)
    }

    override fun onNext(result: T) {

    }

    @CallSuper
    override fun onError(e: Throwable) {
        when (e) {
            is HttpException -> {
                val body = e.response().errorBody()!!.string()
                try {
                    val responseBody: ResponseBody = Gson().fromJson(body, ResponseBody::class.java)
                    view.taskDidError("${responseBody.statusMessage}\nCode: ${responseBody.statusCode}")
                } catch (e: Exception) {
                    view.taskDidError(e.localizedMessage)
                }
            }
            else -> {
                view.taskDidError(e.localizedMessage)
                if (BuildConfig.DEBUG) {
                    e.printStackTrace()
                }
            }
        }
    }

}