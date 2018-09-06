package com.spoonart.movieclip.model

import com.google.gson.annotations.SerializedName
import com.orm.dsl.Table

@Table
class Session {
    @SerializedName("success")
    val isSuccess: Boolean = false

    @SerializedName("session_id")
    val sessionId: String = ""

}
