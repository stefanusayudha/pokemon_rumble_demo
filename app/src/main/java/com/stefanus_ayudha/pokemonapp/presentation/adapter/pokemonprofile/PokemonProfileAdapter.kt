package com.stefanus_ayudha.pokemonapp.presentation.adapter.pokemonprofile

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.stefanus_ayudha.pokemonapp.R
import com.stefanus_ayudha.pokemonapp.databinding.CardPokemonProfileBinding
import com.stefanus_ayudha.pokemonapp.domain.model.PokemonDataModel
import com.stefanus_ayudha.pokemonapp.util.animateClick
import com.stefanus_ayudha.pokemonapp.util.dp
import com.stefanus_ayudha.pokemonapp.util.getScreenWidth
import com.stefanus_ayudha.pokemonapp.util.typeColorMap


class PokemonProfileAdapter(
    private val goToDetail: (PokemonDataModel?) -> Unit
) : BaseQuickAdapter<PokemonDataModel?, QuickViewHolder>() {

    enum class VIEW_TYPE {
        LOADING,
        CONTENT
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return when (viewType) {
            VIEW_TYPE.LOADING.ordinal -> QuickViewHolder(R.layout.card_pokemon_profile, parent)
            else -> PokemonDataViewHolder(parent, goToDetail)
        }
    }

    override fun getItemViewType(position: Int, list: List<PokemonDataModel?>): Int {
        return if (list[position] == null) VIEW_TYPE.LOADING.ordinal
        else VIEW_TYPE.CONTENT.ordinal
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        when (holder.layoutPosition) {
            0 -> holder.itemView.apply {
                layoutParams.apply {
                    this as MarginLayoutParams
                    setMargins(56.dp, 0, 0, 0)
                    width = context.getScreenWidth() - 128.dp
                }
            }
            itemCount - 1 -> holder.itemView.apply {
                layoutParams.apply {
                    this as MarginLayoutParams
                    setMargins(0, 0, 56.dp, 0)
                    width = context.getScreenWidth() - 128.dp
                }
            }
            else -> holder.itemView.apply {
                layoutParams.apply {
                    this as MarginLayoutParams
                    setMargins(0.dp, 0, 0, 0)
                    width = context.getScreenWidth() - 128.dp
                }
            }
        }
    }

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: PokemonDataModel?) {
        if (holder is PokemonDataViewHolder)
            holder.bind(item, position)
    }
}

class PokemonDataViewHolder(
    parent: ViewGroup,
    private val goToDetail: (PokemonDataModel?) -> Unit
) : QuickViewHolder(R.layout.card_pokemon_profile, parent) {

    private var binding: CardPokemonProfileBinding =
        CardPokemonProfileBinding.bind(itemView)
    private var item: PokemonDataModel? = null
    private var isZoomIn = false
    private var scaleAnimation: ScaleAnimation? = null
    private val glide by lazy { Glide.with(binding.root) }

    fun bind(
        item: PokemonDataModel?,
        position: Int
    ) {
        reset()
        this.item = item
        setupName()
        setupImage()
        setupBackgroundColor()
        setupClick()
        // to work properly need to call it once
        animateClickAction()
        showImageLoading()
    }

    private fun reset() {
        isZoomIn = false
        item = null
        scaleAnimation = null
        with(binding.pokemonImage) {
            scaleX = 1f
            scaleY = 1f
            setImageResource(R.drawable.img_blank)
        }
        showImageLoading()
    }

    private fun setupName() {
        binding.pokemonName
            .text = item?.name ?: "Loading.."
    }

    private fun setupImage() {
        showImageLoading()

        glide.load(this.item?.sprites?.other?.home?.frontDefault)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    showImageLoading()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    // extra safety
                    if (item == null)
                        showImageLoading()
                    else
                        hideImageLoading(1000)
                    return false
                }

            })
            .into(binding.pokemonImage)
    }

    private fun setupBackgroundColor() {
        binding.card.setCardBackgroundColor(
            typeColorMap[item?.types?.first()?.type?.name ?: "unknown"] ?: Color.WHITE
        )
    }

    private fun hideImageLoading(delay: Long = 0) {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                binding.pokemonImageLoading.visibility = View.GONE
                binding.pokemonImage.visibility = View.VISIBLE
            }, delay
        )
    }

    private fun showImageLoading() {
        binding.pokemonImageLoading.visibility = View.VISIBLE
        binding.pokemonImage.visibility = View.INVISIBLE
    }

    private fun setupClick() {
        binding.pokemonImage.setOnClickListener {
            animateClickAction()
        }
        binding.btnDetail.setOnClickListener {
            it.animateClick {
                goToDetail.invoke(item)
            }
        }
    }

    private fun animateClickAction() {
        scaleAnimation?.cancel()
        val from = if (!isZoomIn) 1.5f else 1f
        val to = if (!isZoomIn) 1f else 1.5f
        val px = binding.pokemonImage.width / 2f
        val py = binding.pokemonImage.height / 2f
        scaleAnimation = ScaleAnimation(
            from, to,
            from, to,
            px, py
        )

        scaleAnimation?.setAnimationListener(
            object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    isZoomIn = !isZoomIn
                }

                override fun onAnimationRepeat(animation: Animation?) {

                }

            })

        scaleAnimation?.duration = 300
        scaleAnimation?.fillAfter = true

        binding.pokemonImage.startAnimation(scaleAnimation)
    }
}