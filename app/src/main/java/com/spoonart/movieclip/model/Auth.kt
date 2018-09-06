package com.spoonart.movieclip.model

import com.google.gson.annotations.SerializedName
import com.orm.dsl.Table

@Table
class Auth {
    @SerializedName("success")
    val isSuccess: Boolean = false

    @SerializedName("expires_at")
    val expiresAt: String = ""

    @SerializedName("request_token")
    val requestToken: String = ""
}
