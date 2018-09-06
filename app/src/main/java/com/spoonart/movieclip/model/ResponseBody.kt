package com.spoonart.movieclip.model

import com.google.gson.annotations.SerializedName

data class ResponseBody(
        @SerializedName("status_code") val statusCode: Int,
        @SerializedName("status_message") val statusMessage: String
)