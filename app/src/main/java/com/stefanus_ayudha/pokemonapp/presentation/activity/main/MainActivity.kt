package com.stefanus_ayudha.pokemonapp.presentation.activity.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stefanus_ayudha.pokemonapp.databinding.ActivityMainBinding
import com.stefanus_ayudha.pokemonapp.domain.model.PokemonDataModel
import com.stefanus_ayudha.pokemonapp.presentation.activity.profile.ProfileLauncher
import com.stefanus_ayudha.pokemonapp.presentation.adapter.pokemonprofile.PokemonProfileAdapter
import com.stefanus_ayudha.pokemonapp.util.DataState
import com.stefanus_ayudha.pokemonapp.util.dp
import com.stefanus_ayudha.pokemonapp.util.getScreenWidth
import com.stefanus_ayudha.pokemonapp.util.observeSoftInputVisibility
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PokemonProfileAdapter
    private val viewModel: MainViewModel by viewModel()
    private var safeScrolling = false
    private var canLoadMore = true

    private val profileLauncher = registerForActivityResult(ProfileLauncher()) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUI()
        initData()
        initObserver()
        initAction()
    }

    private fun initUI() {
        setupBinding()
        setContentView(binding.root)
        setupRecycleView()
    }

    private fun initData() {
        viewModel.getPokemonList()
    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.getPokemonListState
                .collect(getPokemonCollector())
        }

        lifecycleScope.launch {
            viewModel.loadMoreState
                .collect(loadMoreCollector())
        }

        binding.observeSoftInputVisibility(
            onVisible = {
                binding.pokemonTitle.visibility = View.INVISIBLE
                binding.pokemonStage.visibility = View.INVISIBLE
            },
            onHidden = {
                binding.pokemonTitle.visibility = View.VISIBLE
                binding.pokemonStage.visibility = View.VISIBLE
            }
        )
    }

    private fun initAction() {
        binding.layoutPokemon.rvPokemon
            .addOnScrollListener(scrollListener)

        binding.search.doAfterTextChanged {
            binding.search.setSelection(it?.length ?: 0)
            val names = adapter.items.filterNotNull().map {
                it.name
            }

            for (i in names.indices) {
                if (names[i]?.lowercase()?.contains(it.toString()) == true) {
                    binding.layoutPokemon.rvPokemon.smoothScrollToPosition(i)
                    break
                }
            }
        }
    }

    /**
     * ## Section UI
     */
    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
    }

    private fun setupRecycleView() {
        adapter = PokemonProfileAdapter(::gotoDetailPokemon)
        adapter.items = listOf()
        binding.layoutPokemon.rvPokemon.adapter = adapter

        binding.layoutPokemon.rvPokemon.addOnChildAttachStateChangeListener(
            object : RecyclerView.OnChildAttachStateChangeListener {
                override fun onChildViewAttachedToWindow(view: View) {
                    if (binding.layoutPokemon.rvPokemon.getChildAt(1) == view) {
                        binding.layoutPokemon.rvPokemon.smoothScrollBy(100, 0)
                    }
                }

                override fun onChildViewDetachedFromWindow(view: View) {

                }
            }
        )
    }

    /**
     * ## Section Observer
     */
    private fun getPokemonCollector() =
        FlowCollector<DataState<List<PokemonDataModel>>> {
            when (it) {
                is DataState.Loading -> showBootstrapLoading()
                is DataState.Error -> showDialogError(it.cause)
                is DataState.Success -> displayPokemonList(it.data)
                else -> {}
            }
        }

    private fun loadMoreCollector() =
        FlowCollector<DataState<List<PokemonDataModel>>> {
            when (it) {
                is DataState.Error -> showDialogError(it.cause)
                is DataState.Success -> displayLoadMorePokemon(it.data)
                else -> {}
            }
        }


    private fun showBootstrapLoading() {
        binding.layoutPokemonFlipper.displayedChild = 1
    }

    private fun showContent() {
        binding.layoutPokemonFlipper.displayedChild = 0
    }

    private fun showDialogError(cause: Throwable?) {
        Toast.makeText(this, "${cause?.message}", Toast.LENGTH_SHORT).show()
    }

    private fun displayPokemonList(data: List<PokemonDataModel?>) {
        adapter.items = data.plus(null)
        showContent()
    }

    private fun displayLoadMorePokemon(data: List<PokemonDataModel?>) {
        adapter.removeAt(adapter.itemCount - 1)
        adapter.addAll(data)
        adapter.add(null)
        canLoadMore = true
    }

    /**
     * ## Section Action
     */
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            when (newState) {
                RecyclerView.SCROLL_STATE_SETTLING -> onSettle(recyclerView)
                RecyclerView.SCROLL_STATE_DRAGGING -> onDrag(recyclerView)
                RecyclerView.SCROLL_STATE_IDLE -> onIdle(recyclerView)
            }
        }

        private fun onDrag(recyclerView: RecyclerView) {
            // Log.d("TAG", "onDrag: ${recyclerView.computeHorizontalScrollOffset()}")
        }

        private fun onSettle(recyclerView: RecyclerView) {
            // Log.d("TAG", "onSettle: ${recyclerView.computeHorizontalScrollOffset()}")
        }

        private fun onIdle(recyclerView: RecyclerView) {
            if (safeScrolling)
                safeScrolling = false
            else
                focusToCenterCard(recyclerView)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (adapter.items.isNotEmpty() && canLoadMore) {
                loadMoreHandler(recyclerView)
            }
            animateScrolling(recyclerView)
        }
    }

    private fun focusToCenterCard(recyclerView: RecyclerView) {
        val lm = recyclerView.layoutManager as LinearLayoutManager
        val visibleStartIndex = lm.findFirstVisibleItemPosition()
        val visibleEndIndex = lm.findLastVisibleItemPosition()

        // recycle view will destroy invisible item
        // misal di layar hanya ada 3 item terlihat
        // sekalipun posisi item pada posisi ke 10
        // tetapi getChildAt hanya mengetahui 3 item saja yang terlihat di layar saja
        // maka dari itu last visible index tetaplah selalu 3
        val lastVisibleViewIndex = visibleEndIndex - visibleStartIndex

        val lastVisibleChild = recyclerView.getChildAt(lastVisibleViewIndex)
        val lastVisibleChildOffset = lastVisibleChild.x

        // jika last item ofset < setengah layar
        // scroll ke kiri sampai child ofset = 56dp

        // jika last item ofser > setegah layar
        // scroll ke kanan hingga ofset = screenwidth - 56dp

        safeScrolling = true
        val scrollTarget =
            if (lastVisibleChildOffset < getScreenWidth() / 2)
                56.dp
            else
                getScreenWidth() - 56.dp

        val shouldScrollBy = (scrollTarget - lastVisibleChildOffset) * -1

        recyclerView.smoothScrollBy(
            shouldScrollBy.toInt(), 0
        )
    }

    private fun getCenteredCardIndex(recyclerView: RecyclerView): Int {

        val lm = recyclerView.layoutManager as LinearLayoutManager
        val visibleStartIndex = lm.findFirstVisibleItemPosition()
        val visibleEndIndex = lm.findLastVisibleItemPosition()

        // recycle view will destroy invisible item
        // misal di layar hanya ada 3 item terlihat
        // sekalipun posisi item pada posisi ke 10
        // tetapi getChildAt hanya mengetahui 3 item saja yang terlihat di layar saja
        // maka dari itu last visible index tetaplah selalu 3
        val lastVisibleViewIndex = visibleEndIndex - visibleStartIndex

        val lastVisibleChild = recyclerView.getChildAt(lastVisibleViewIndex)
        val lastVisibleChildOffset = lastVisibleChild.x

        // jika visible area dari child lebih dari setengah layar
        // item akan di tandai sebagai fokus
        val focusedItem =
            if (lastVisibleChildOffset < getScreenWidth() / 2)
                visibleEndIndex
            else
                visibleStartIndex

        Log.d("TAG", "getCenteredCardIndex: $focusedItem")
        return focusedItem
    }

    private fun loadMoreHandler(recyclerView: RecyclerView) {
        canLoadMore = false
        val ll = recyclerView.layoutManager as LinearLayoutManager
        if (ll.findLastVisibleItemPosition() == adapter.itemCount - 1) {
            requestLoadMore()
        } else {
            canLoadMore = true
        }
    }

    private fun animateScrolling(recyclerView: RecyclerView) {
        val ll = recyclerView.layoutManager as LinearLayoutManager

        val fistIndex = ll.findFirstVisibleItemPosition()
        val lastIndex = ll.findLastVisibleItemPosition()

        scaleFirstChild(recyclerView.getChildAt(0))
        scaleLastChild(recyclerView.getChildAt(lastIndex - fistIndex))
    }

    private fun scaleFirstChild(view: View) {
        // if visible offset < setengah layar
        // scale down
        if (view.x + view.width < getScreenWidth() / 2)
            animateScaleDown(view)
        else
            animateScaleUp(view)
    }

    private fun scaleLastChild(view: View) {
        // if visible ofset < setengah layar
        // scale down

        if ((getScreenWidth() - view.x) < getScreenWidth() / 2) {
            animateScaleDown(view)
        } else {
            animateScaleUp(view)
        }
    }

    private fun animateScaleDown(view: View) {
        view.scaleY = .8f
        view.scaleX = .8f
    }

    private fun animateScaleUp(view: View) {
        view.scaleY = 1f
        view.scaleX = 1f
    }

    private fun requestLoadMore() {
        viewModel.loadMorePokemon()
    }

    private fun gotoDetailPokemon(item: PokemonDataModel?) {
        profileLauncher.launch(item)
    }
}