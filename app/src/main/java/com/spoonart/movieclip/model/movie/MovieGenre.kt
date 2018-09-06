package com.spoonart.movieclip.model.movie

import com.google.gson.annotations.SerializedName
import com.orm.dsl.Table
import com.orm.dsl.Unique


data class MovieGenre(
        @SerializedName("genres") val genres: ArrayList<Genre>
)

@Table
class Genre {
    @Unique
    @SerializedName("id")
    val genreId: Int = 0

    @SerializedName("name")
    val name: String = ""
}