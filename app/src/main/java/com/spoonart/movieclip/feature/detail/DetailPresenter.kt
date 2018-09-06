package com.spoonart.movieclip.feature.detail

import android.content.Context
import com.spoonart.libs.base.BasePresenter
import com.spoonart.movieclip.extension.getApiKey
import com.spoonart.movieclip.extension.tryCall
import com.spoonart.movieclip.model.movie.MovieDetail
import com.spoonart.movieclip.model.movie.MovieTrailer
import com.spoonart.movieclip.network.ResponseObserver

class DetailPresenter(context: Context, view: DetailContract.View) : BasePresenter<DetailContract.View>(context, view), DetailContract.Presenter {

    override fun requestDetailMovie(movieId: Int) {
        iView.taskDidBegin()
        val req = tryCall(service.getMovieDetails(movieId, getApiKey()))
        req.subscribe(object : ResponseObserver<MovieDetail>(disposeBag, iView) {
            override fun onNext(result: MovieDetail) {
                println("ON NEXT DETAIL!!!!")
                iView.onDetailLoaded(result)
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

    override fun requestTrailer(movieId: Int) {
        iView.taskDidBegin()
        val req = tryCall(service.getMovieTrailer(movieId, getApiKey()))
        req.subscribe(object : ResponseObserver<MovieTrailer>(disposeBag, iView) {
            override fun onNext(result: MovieTrailer) {
                iView.onTrailerLoaded(result)
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

}