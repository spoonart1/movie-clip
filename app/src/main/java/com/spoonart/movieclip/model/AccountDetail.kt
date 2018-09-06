package com.spoonart.movieclip.model

import com.google.gson.annotations.SerializedName
import com.orm.dsl.Table

@Table
class AccountDetail {
    @SerializedName("avatar")
    val gravatar: Gravatar? = null

    @SerializedName("id")
    val accountId: Int = 0

    @SerializedName("iso_639_1")
    val iso6391: String = ""

    @SerializedName("iso_3166_1")
    val iso31661: String = ""

    @SerializedName("name")
    val name: String = ""

    @SerializedName("username")
    val username: String = ""

    @SerializedName("include_adult")
    val isIncludeAdult: Boolean = false
}

@Table
class Gravatar {
    @SerializedName("hash")
    val hash: String = ""
}