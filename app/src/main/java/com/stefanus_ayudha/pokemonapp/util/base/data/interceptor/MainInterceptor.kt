package com.stefanus_ayudha.pokemonapp.util.base.data.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class MainInterceptor: Interceptor {
    // i don't think i need to intercept something
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("x-important-info", "Steve the best")
            .build()
        return chain.proceed(request)
    }
}