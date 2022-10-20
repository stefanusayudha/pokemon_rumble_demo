package com.stefanus_ayudha.pokemonapp.presentation

import com.stefanus_ayudha.pokemonapp.presentation.activity.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { MainViewModel(get(), get()) }
}