package com.spoonart.movieclip.model.movie

import com.google.gson.annotations.SerializedName

data class MovieTrailer(
        @SerializedName("id") val id: Int,
        @SerializedName("results") val results: List<TrailerResult>
)

data class TrailerResult(
        @SerializedName("id") val id: String,
        @SerializedName("iso_639_1") val iso639_1: String,
        @SerializedName("iso_3166_1") val iso3166_1: String,
        @SerializedName("key") val key: String,
        @SerializedName("name") val name: String,
        @SerializedName("site") val site: String,
        @SerializedName("size") val size: Int,
        @SerializedName("type") val type: String
)