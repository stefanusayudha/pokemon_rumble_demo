package com.stefanus_ayudha.pokemonapp.presentation.activity.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stefanus_ayudha.pokemonapp.domain.model.PokemonDataModel
import com.stefanus_ayudha.pokemonapp.domain.payload.GetPokemonListPld
import com.stefanus_ayudha.pokemonapp.domain.repository.PokemonDataRepository
import com.stefanus_ayudha.pokemonapp.domain.repository.PokemonListRepository
import com.stefanus_ayudha.pokemonapp.util.DataState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val pokemonListRepository: PokemonListRepository,
    private val pokemonDataRepository: PokemonDataRepository
) : ViewModel() {

    private var getPokemonListJob: Job? = null
    private val _getPokemonListState =
        MutableStateFlow<DataState<List<PokemonDataModel>>>(DataState.Default())
    val getPokemonListState: StateFlow<DataState<List<PokemonDataModel>>>
        get() = _getPokemonListState
    private var getPokemonListPld = GetPokemonListPld()

    fun getPokemonList() {
        getPokemonListJob?.cancel()
        _getPokemonListState.value = DataState.Loading()

        // setup payload
        getPokemonListPld = getPokemonListPld.copy(
            page = 1
        )

        getPokemonListJob = viewModelScope.launch {
            kotlin.runCatching {
                pokemonListRepository.getPokemonList(
                    getPokemonListPld
                )
            }.onSuccess {
                _getPokemonListState.value = DataState.Success(it)
            }.onFailure {
                _getPokemonListState.value = DataState.Error(it)
            }
        }
    }

    private val _loadMoreState =
        MutableStateFlow<DataState<List<PokemonDataModel>>>(DataState.Default())
    val loadMoreState: StateFlow<DataState<List<PokemonDataModel>>>
        get() = _loadMoreState

    private var loadMorePokemonJob: Job? = null
    fun loadMorePokemon() {

        loadMorePokemonJob?.cancel()
        _loadMoreState.value = DataState.Loading()

        // setup payload
        getPokemonListPld = getPokemonListPld.copy(
            page = getPokemonListPld.page + 1
        )

        loadMorePokemonJob = viewModelScope.launch {
            kotlin.runCatching {
                pokemonListRepository.getPokemonList(
                    getPokemonListPld
                )
            }.onSuccess {
                _loadMoreState.value = DataState.Success(it)
            }.onFailure {
                _loadMoreState.value = DataState.Error(it)
            }
        }
    }
}