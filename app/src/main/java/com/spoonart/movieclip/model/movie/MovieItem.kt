package com.spoonart.movieclip.model.movie

import com.google.gson.annotations.SerializedName

data class MovieItem(
        @SerializedName("page") val page: Int,
        @SerializedName("total_result") val totalResult: Int,
        @SerializedName("total_pages") val totalPage: Int,
        @SerializedName("results") val results: ArrayList<BaseMovieResult>
)

