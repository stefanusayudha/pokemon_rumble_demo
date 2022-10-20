package com.stefanus_ayudha.pokemonapp.presentation.activity.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.core.text.bold
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.stefanus_ayudha.pokemonapp.databinding.ActivityProfileBinding
import com.stefanus_ayudha.pokemonapp.domain.model.*
import com.stefanus_ayudha.pokemonapp.util.animateClick
import java.io.File
import java.io.FileOutputStream

class ProfileActivity : Activity() {

    enum class PAYLOAD {
        DATA
    }

    private lateinit var binding: ActivityProfileBinding
    private lateinit var data: PokemonDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initUI()
        initAction()
    }

    private fun initData() {
        val dataString = intent.getStringExtra(PAYLOAD.DATA.name)
        data = Gson().fromJson(dataString, PokemonDataModel::class.java)
    }

    private fun initUI() {
        setupBinding()
        setupContentView()
        setupImage()
        showDetail()
    }

    private fun initAction() {
        binding.btnShare.setOnClickListener {
            it.animateClick {
                sharePokemon()
            }
        }
    }

    private fun setupBinding() {
        binding = ActivityProfileBinding.inflate(layoutInflater)
    }

    private fun setupContentView() {
        setContentView(binding.root)
    }

    private fun setupImage() {
        Glide.with(applicationContext)
            .load(data.sprites?.other?.home?.frontDefault)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    hideBootstrapLoading(2000)
                    return false
                }
            })
            .into(binding.image)
    }

    private fun showDetail() {
        val text = SpannableStringBuilder()
            .bold { append("Name : ") }
            .append(data.name)
            .append("\n")
            .bold { append("Species : ") }
            .append(data.species?.name)
            .append("\n")
            .bold { append("Type : ") }
            .append(
                data.types?.map { it?.type?.name }
                    ?.joinToString(separator = ", ") {
                        it.orEmpty()
                    }
            )
            .append("\n")
            .bold { append("Height: ") }
            .append("${data.height}")
            .append("\n")
            .bold { append("Weight: ") }
            .append("${data.weight}")
            .append("\n")
            .bold { append("Experience : ") }
            .append("${data.baseExperience}")
            .append("\n")
            .bold { append("Ability : ") }
            .append(
                data.abilities?.map {
                    it?.ability?.name
                }?.joinToString(", ") {
                    it.orEmpty()
                }
            )
            .append("\n")
            .bold { append("Status : ") }
            .append(
                data.stats?.map {
                    "${it?.stat?.name} (${it?.baseStat})"
                }?.joinToString(", ") {
                    it
                }
            )

        binding.detail.text = text
    }

    private fun showBootstrapLoading() {
        binding.imageLoading.visibility = View.VISIBLE
        binding.image.visibility = View.INVISIBLE
    }

    private fun hideBootstrapLoading(delay: Long = 0) {
        Handler(mainLooper).postDelayed(
            {
                binding.imageLoading.visibility = View.GONE
                binding.image.visibility = View.VISIBLE
            }, delay
        )
    }

    private fun sharePokemon() {
        val bitmap = binding.image.drawable.toBitmap()
        shareImageandText(bitmap)
    }

    private fun shareImageandText(bitmap: Bitmap) {
        val uri: Uri? = getmageToShare(bitmap)
        val intent = Intent(Intent.ACTION_SEND)

        // putting uri of image to be shared
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        // adding text to share
        intent.putExtra(Intent.EXTRA_TEXT, binding.detail.text)

        // Add subject Here
        intent.putExtra(Intent.EXTRA_SUBJECT, "POKEMON!")

        // setting type to image
        intent.type = "image/png"

        // calling startactivity() to share
        startActivity(Intent.createChooser(intent, "Share Via"))
    }

    // Retrieving the url to share
    private fun getmageToShare(bitmap: Bitmap): Uri? {
        val imagefolder = File(cacheDir, "images")
        var uri: Uri? = null
        try {
            imagefolder.mkdirs()
            val file = File(imagefolder, "shared_image.png")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream)
            outputStream.flush()
            outputStream.close()
            uri = FileProvider.getUriForFile(this, "com.stefanus_ayudha.pokemonapp", file)
        } catch (e: Exception) {
            Toast.makeText(this, "" + e.message, Toast.LENGTH_LONG).show()
        }
        return uri
    }
}