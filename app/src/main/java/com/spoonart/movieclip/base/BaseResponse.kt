package com.spoonart.movieclip.base

import com.google.gson.annotations.SerializedName

data class BaseResponse(
        @SerializedName("status_code") val statusCode:Int,
        @SerializedName("status_message") val statusMessage:String)