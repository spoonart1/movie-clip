package com.spoonart.movieclip.feature.main.item

import android.content.Context
import com.google.gson.Gson
import com.spoonart.libs.base.BasePresenter
import com.spoonart.movieclip.extension.getApiKey
import com.spoonart.movieclip.extension.tryCall
import com.spoonart.movieclip.model.movie.MovieItem
import com.spoonart.movieclip.model.movie.MovieFavoriteBody
import com.spoonart.movieclip.model.ResponseBody
import com.spoonart.movieclip.network.ResponseObserver
import com.spoonart.movieclip.util.AuthUtil
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.RequestBody

class MoviePresenter(context: Context, view: MovieContract.MovieView) : BasePresenter<MovieContract.MovieView>(context, view), MovieContract.Presenter {

    override fun loadMovie(page: Int, type: MovieType) {
        iView.taskDidBegin()
        val req = getRequest(page, type)
        req.subscribe(object : ResponseObserver<MovieItem>(disposeBag, iView) {
            override fun onNext(result: MovieItem) {
                iView.onMovieLoaded(result)
            }

            override fun onComplete() {
                super.onComplete()
                iView.taskDidFinish()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                iView.taskDidFinish()
            }
        })
    }

    override fun loadMovieMore(page: Int, type: MovieType) {
        iView.taskDidBegin()
        val req = getRequest(page, type)
        req.subscribe(object : ResponseObserver<MovieItem>(disposeBag, iView) {
            override fun onNext(result: MovieItem) {
                iView.onMovieLoadedMore(page, result.totalPage, result)
            }

            override fun onComplete() {
                super.onComplete()
                iView.taskDidFinish()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                iView.taskDidFinish()
            }
        })
    }

    override fun setFavoriteMovie(mediaId: Int, isFavorite: Boolean) {
        iView.taskDidBegin()

        val favPojo = MovieFavoriteBody("movie", mediaId, isFavorite)
        val jsonStr = Gson().toJson(favPojo)
        val reqBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr)

        val req = tryCall(service.setMovieAsFavorite(AuthUtil.getAccountId(), getApiKey(), AuthUtil.getSessionId(), reqBody))
        req.subscribe(object : ResponseObserver<ResponseBody>(disposeBag, iView) {
            override fun onNext(result: ResponseBody) {
                iView.onMovieFavorited(result.statusMessage)
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

    private fun getRequest(page: Int, type: MovieType): Observable<MovieItem> {
        val req: Observable<MovieItem>
        when (type) {
            MovieType.Popular -> {
                req = tryCall(service.requestPopularMovies(getApiKey(), page))
            }

            MovieType.TopRated -> {
                req = tryCall(service.requestTopRatedMovies(getApiKey(), page))
            }

            MovieType.Favorite -> {
                val accountId = AuthUtil.getAccountId()
                req = tryCall(service.requestFavorited(accountId, getApiKey(), AuthUtil.getSessionId(), page))
            }
        }
        return req
    }

}