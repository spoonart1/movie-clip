package com.spoonart.movieclip.model.movie

import com.google.gson.annotations.SerializedName

data class MovieDetail(
        @SerializedName("budget") val budget: Double,
        @SerializedName("belongs_to_collection") val belongsCollection: BelongsCollection,
        @SerializedName("genres") val genres: List<Genre>,
        @SerializedName("homepage") val homePage: String,
        @SerializedName("status") val status: String,
        @SerializedName("tag_line") val tagLine: String,
        @SerializedName("imdb_id") val imdbId: String,
        @SerializedName("production_companies") val productionCompanies: List<ProductionCompany>,
        @SerializedName("production_countries") val productionCountries: List<ProductionCountry>,
        @SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguage>

) : BaseMovieResult()

data class BelongsCollection(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String,
        @SerializedName("poster_path") val posterPath: String,
        @SerializedName("backdrop_path") val backdropPath: String
)

data class ProductionCompany(
        @SerializedName("id") val id: Int,
        @SerializedName("logo_path") val logoPath: String,
        @SerializedName("name") val name: String,
        @SerializedName("origin_country") val originCountry: String
)

data class ProductionCountry(
        @SerializedName("iso_3166_1") val iso3166_1: String,
        @SerializedName("name") val name: String
)

data class SpokenLanguage(
        @SerializedName("iso_639_1") val iso639_1: String,
        @SerializedName("name") val name: String
)