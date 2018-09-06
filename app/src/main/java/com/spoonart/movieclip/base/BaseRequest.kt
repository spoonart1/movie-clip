package com.spoonart.movieclip.base

interface BaseRequest {
    fun createRequestToken() {}
    fun createSessionID(requestToken:String) {}
}