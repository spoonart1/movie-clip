package com.spoonart.movieclip.network

import com.spoonart.movieclip.model.*
import com.spoonart.movieclip.model.movie.MovieDetail
import com.spoonart.movieclip.model.movie.MovieGenre
import com.spoonart.movieclip.model.movie.MovieItem
import com.spoonart.movieclip.model.movie.MovieTrailer
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

interface RestAPI {

    @GET("authentication/token/new")
    fun requestToken(
            @Query("api_key") apiKey: String
    ): Observable<Auth>

    @GET("genre/movie/list")
    fun requestAllGenre(
            @Query("api_key") apiKey: String,
            @Query("language") language: String = "en-US"
    ): Observable<MovieGenre>

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("authentication/token/validate_with_login")
    fun requestSessionWithLogin(
            @Field("username") username: String,
            @Field("password") password: String,
            @Field("request_token") requestToken: String,
            @Query("api_key") apiKey: String
    ): Observable<Auth>

    @FormUrlEncoded
    @POST("authentication/session/new")
    fun requestSessionId(
            @Field("request_token") requestToken: String,
            @Query("api_key") apiKey: String
    ): Observable<Session>

    @GET("account")
    fun getAccountDetail(
            @Query("api_key") apiKey: String,
            @Query("session_id") sessionId: String
    ): Observable<AccountDetail>

    @GET("movie/popular")
    fun requestPopularMovies(
            @Query("api_key") apiKey: String,
            @Query("page") page: Int
    ): Observable<MovieItem>

    @GET("movie/top_rated")
    fun requestTopRatedMovies(
            @Query("api_key") apiKey: String,
            @Query("page") page: Int
    ): Observable<MovieItem>

    @GET("account/{account_id}/favorite/movies")
    fun requestFavorited(
            @Path("account_id") accountId: Int,
            @Query("api_key") apiKey: String,
            @Query("session_id") sessionId: String,
            @Query("page") page: Int,
            @Query("language") language: String = "en-US"
    ): Observable<MovieItem>


    @POST("account/{account_id}/favorite")
    fun setMovieAsFavorite(
            @Path("account_id") accountId: Int,
            @Query("api_key") apiKey: String,
            @Query("session_id") sessionId: String,
            @Body requestBody: RequestBody
    ): Observable<ResponseBody>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
            @Path("movie_id") movieId: Int,
            @Query("api_key") apiKey: String,
            @Query("language") language: String = "en-US"
    ): Observable<MovieDetail>

    @GET("movie/{movie_id}/videos")
    fun getMovieTrailer(
            @Path("movie_id") movieId: Int,
            @Query("api_key") apiKey: String,
            @Query("language") language: String = "en-US"
    ): Observable<MovieTrailer>
}