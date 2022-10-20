package com.stefanus_ayudha.pokemonapp.presentation.activity.profile

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.gson.Gson
import com.stefanus_ayudha.pokemonapp.domain.model.PokemonDataModel

class ProfileLauncher : ActivityResultContract<PokemonDataModel, Any>() {
    override fun createIntent(context: Context, input: PokemonDataModel): Intent {
        return Intent(context, ProfileActivity::class.java).apply {
            putExtra(ProfileActivity.PAYLOAD.DATA.name, Gson().toJson(input))
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Any {
        return Unit
    }
}