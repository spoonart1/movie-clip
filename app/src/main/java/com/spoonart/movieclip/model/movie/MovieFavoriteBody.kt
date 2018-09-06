package com.spoonart.movieclip.model.movie

import com.google.gson.annotations.SerializedName

data class MovieFavoriteBody(
        @SerializedName("media_type") val mediaType:String,
        @SerializedName("media_id") val mediaId:Int,
        @SerializedName("favorite") val favorite:Boolean
)