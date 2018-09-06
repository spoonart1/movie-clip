package com.spoonart.movieclip.feature.main

import android.content.Context
import com.spoonart.libs.base.BasePresenter
import com.spoonart.movieclip.extension.getApiKey
import com.spoonart.movieclip.extension.tryCall
import com.spoonart.movieclip.custom.pager.CommonFragmentViewModel
import com.spoonart.movieclip.feature.main.item.MovieFragment
import com.spoonart.movieclip.feature.main.item.MovieType
import com.spoonart.movieclip.model.AccountDetail
import com.spoonart.movieclip.model.movie.MovieGenre
import com.spoonart.movieclip.network.ResponseObserver
import com.spoonart.movieclip.util.DBUtil

class MainPresenter(context: Context, view: MainContract.View) : BasePresenter<MainContract.View>(context, view), MainContract.Presenter {

    override fun makeFragment() {
        val items = createFragmenModel()
        iView.onFragmentsCreated(items)
    }

    override fun requestGenre() {
        iView.taskDidBegin()
        val req = tryCall(service.requestAllGenre(getApiKey()))
        req.subscribe(object : ResponseObserver<MovieGenre>(disposeBag, iView) {
            override fun onNext(result: MovieGenre) {
                saveGenre(result)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                iView.taskDidFinish()
            }
        })
    }

    override fun requestAccountDetail(sessionId:String) {
        iView.taskDidBegin()
        val req = tryCall(service.getAccountDetail(getApiKey(), sessionId))
        req.subscribe(object : ResponseObserver<AccountDetail>(disposeBag, iView){
            override fun onNext(result: AccountDetail) {
                DBUtil.saveAccountData(result)
                iView.onAccountDetailFetced()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                iView.taskDidFinish()
            }
        })
    }

    private fun saveGenre(result: MovieGenre) {
        DBUtil.saveGenres(result.genres)
    }

    private fun createFragmenModel(): List<CommonFragmentViewModel> {
        return listOf(
                CommonFragmentViewModel("POPULAR", MovieFragment.make(MovieType.Popular)),
                CommonFragmentViewModel("TOP RATED", MovieFragment.make(MovieType.TopRated)),
                CommonFragmentViewModel("FAVORITE", MovieFragment.make(MovieType.Favorite))
        )
    }

}