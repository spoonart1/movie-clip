package com.spoonart.libs.base

import android.content.Context
import com.spoonart.movieclip.extension.getApiKey
import com.spoonart.movieclip.extension.tryCall
import com.spoonart.movieclip.model.Session
import com.spoonart.movieclip.network.ApiConfig
import com.spoonart.movieclip.network.ResponseObserver
import com.spoonart.movieclip.network.RestAPI
import com.spoonart.movieclip.util.DBUtil
import com.spoonart.movieclip.util.NetworkUtil
import io.reactivex.disposables.CompositeDisposable


/**
 * Created by Lafran on 7/25/17.
 */

typealias AuthCallBack = (String) -> Unit


open class BasePresenter<T : IBaseView>(val context: Context, val iView: T) {


    var deviceId: String = "no-device-id"
    var service: RestAPI
    var disposeBag: CompositeDisposable = CompositeDisposable()

    init {
        service = ApiConfig.getService(context)
    }

    fun isNetworkAvailable(): Boolean {
        return NetworkUtil.isConnected(context)
    }

    fun requestSessionId(callback: AuthCallBack) {
        iView.taskDidBegin()
        tryCall(service.requestToken(getApiKey()))
                .flatMap { it ->
                    return@flatMap tryCall(service.requestSessionWithLogin("spoonart", "fr33fall", it.requestToken, getApiKey()))
                }
                .flatMap { auth ->
                    DBUtil.saveToken(auth)
                    return@flatMap tryCall(service.requestSessionId(auth.requestToken, getApiKey()))
                }
                .subscribe(object : ResponseObserver<Session>(disposeBag, iView) {
                    override fun onNext(result: Session) {
                        DBUtil.saveSession(result)
                        callback(result.sessionId)
                    }

                    override fun onComplete() {
                        iView.taskDidFinish()
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        iView.taskDidFinish()
                    }
                })
    }

    fun disposeAllRequest() {
        if (!disposeBag.isDisposed) {
            disposeBag.dispose()
        }
    }

}
