package com.stefanus_ayudha.pokemonapp.data

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.stefanus_ayudha.pokemonapp.data.source.PokemonDataSource
import com.stefanus_ayudha.pokemonapp.data.source.PokemonDataSourceImpl
import com.stefanus_ayudha.pokemonapp.data.source.web.PokemonWebSource
import com.stefanus_ayudha.pokemonapp.data.source.web.usecase.PokemonDataWeb
import com.stefanus_ayudha.pokemonapp.data.source.web.usecase.PokemonListWeb
import com.stefanus_ayudha.pokemonapp.domain.repository.PokemonDataRepository
import com.stefanus_ayudha.pokemonapp.domain.repository.PokemonListRepository
import com.stefanus_ayudha.pokemonapp.util.base.data.URLs
import com.stefanus_ayudha.pokemonapp.util.base.data.interceptor.MainInterceptor
import com.stefanus_ayudha.pokemonapp.util.createRetrofitService
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.binds
import org.koin.dsl.module

val dataModule = module {
    ///// OKHTTP
    single {
        ChuckerCollector(
            context = androidContext(),
            // Toggles visibility of the notification
            showNotification = true,
            // Allows to customize the retention period of collected data
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )
    }

    single {
        ChuckerInterceptor.Builder(context = androidApplication())
            // The previously created Collector
            .collector(get())
            // The max body content length in bytes, after this responses will be truncated.
            .maxContentLength(250_000L)
            // List of headers to replace with ** in the Chucker UI
            // .redactHeaders("Auth-Token", "Bearer")
            // Read the whole response body even when the client does not consume the response completely.
            // This is useful in case of parsing errors or when the response body
            // is closed before being read like in Retrofit with Void and Unit types.
            .alwaysReadResponseBody(true)
            // Use decoder when processing request and response bodies. When multiple decoders are installed they
            // are applied in an order they were added.
            // .addBodyDecoder(decoder)
            // Controls Android shortcut creation. Available in SNAPSHOTS versions only at the moment
            // .createShortcut(true)
            .build()
    }

    factory {
        OkHttpClient.Builder()
            .addInterceptor(get<ChuckerInterceptor>())
            .addInterceptor(MainInterceptor())
            .build()
    }

    single {
        createRetrofitService(
            PokemonWebSource::class.java,
            get(),
            URLs.BASE_POKEMON
        )
    } binds arrayOf(
        PokemonListWeb::class,
        PokemonDataWeb::class
    )

    single<PokemonDataSource> {
        PokemonDataSourceImpl(
            get(),
            get()
        )
    } binds arrayOf(
        PokemonListRepository::class,
        PokemonDataRepository::class
    )
}