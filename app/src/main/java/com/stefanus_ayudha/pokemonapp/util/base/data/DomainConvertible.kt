package com.stefanus_ayudha.pokemonapp.util.base.data

interface DomainConvertible<DOMAIN_MODEL> {
    fun toDomain(): DOMAIN_MODEL
}