package com.spoonart.movieclip.feature.detail

import com.spoonart.libs.base.IBaseView
import com.spoonart.movieclip.model.movie.MovieDetail
import com.spoonart.movieclip.model.movie.MovieTrailer

class DetailContract {


    interface View : IBaseView {
        fun onDetailLoaded(detail: MovieDetail)
        fun onTrailerLoaded(trailer: MovieTrailer)
    }

    interface Presenter {
        fun requestDetailMovie(movieId: Int)
        fun requestTrailer(movieId: Int)
    }

}