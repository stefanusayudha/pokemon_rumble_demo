package com.stefanus_ayudha.pokemonapp.util

import androidx.annotation.NonNull
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun <I> createRetrofitService(
    @NonNull service: Class<I>,
    httpClient: OkHttpClient,
    baseUrl: String,
): I = Retrofit.Builder()
    .client(httpClient)
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(service)