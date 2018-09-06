package com.spoonart.movieclip.feature.main.item

import com.spoonart.libs.base.IBaseView
import com.spoonart.movieclip.base.BaseRequest
import com.spoonart.movieclip.model.movie.MovieItem

class MovieContract {

    interface MovieView : IBaseView {
        fun onMovieLoaded(movies: MovieItem)
        fun onMovieLoadedMore(currentPage: Int, totalPage: Int, movies: MovieItem)
        fun onMovieFavorited(message:String)
        fun onReload()
    }

    interface Presenter : BaseRequest {
        fun loadMovie(page: Int, type: MovieType)
        fun loadMovieMore(page: Int, type: MovieType)
        fun setFavoriteMovie(mediaId: Int, isFavorite: Boolean)
    }

}