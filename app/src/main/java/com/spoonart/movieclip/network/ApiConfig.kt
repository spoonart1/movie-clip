package com.spoonart.movieclip.network

import android.content.Context
import com.google.gson.GsonBuilder
import com.spoonart.movieclip.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfig {

    companion object {

        private var retrofit: Retrofit? = null

        val API_KEY = "ac4c2dca1a2d7b9b4a15b24f3ac67a9f"
        val BASE_URL = "https://api.themoviedb.org/3/"
        val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

        private fun instance(context: Context): Retrofit {
            if (retrofit == null)
                retrofit = ApiConfig().getClient(context, BASE_URL)

            return retrofit!!
        }

        fun getService(context: Context): RestAPI {
            return instance(context).create(RestAPI::class.java)
        }
    }

    private fun getClient(context: Context, baseUrl: String): Retrofit {

        val okhttpBuilder = OkHttpClient().newBuilder()
        okhttpBuilder.connectTimeout(60, TimeUnit.SECONDS)
        okhttpBuilder.writeTimeout(60, TimeUnit.SECONDS)
        okhttpBuilder.readTimeout(60, TimeUnit.SECONDS)
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        val cache = Cache(context.cacheDir, cacheSize.toLong())
        okhttpBuilder.cache(cache)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        okhttpBuilder.addInterceptor(interceptor)

        val gson = GsonBuilder()
                .setLenient()
                .create()
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okhttpBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return retrofit!!
    }
}