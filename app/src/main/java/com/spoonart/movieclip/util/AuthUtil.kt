package com.spoonart.movieclip.util

import com.orm.SugarRecord
import com.spoonart.movieclip.model.AccountDetail
import com.spoonart.movieclip.model.Auth
import com.spoonart.movieclip.model.Session

object AuthUtil {

    fun getRequestToken(): String {
        val token = SugarRecord.first(Auth::class.java)
        return token.requestToken
    }

    fun getSessionId(): String {
        val token = SugarRecord.first(Session::class.java)
        return token.sessionId
    }

    fun getAccountId():Int{
        val account = SugarRecord.first(AccountDetail::class.java)
        return account.accountId
    }
}