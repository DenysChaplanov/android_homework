package com.vinted.demovinted.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.vinted.demovinted.R
import com.vinted.demovinted.data.models.CatalogItem
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment: Fragment(R.layout.fragment_feed) {

    private val feedAdapter by lazy { FeedAdapter(::onItemClick) }
    private val feedViewModel by viewModels<FeedViewModel>()

    private lateinit var endlessScrollListener: EndlessScrollListener
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupEndlessScrollListener()

        feedViewModel.loadInitialItems()

        feedViewModel.feedData.observe(viewLifecycleOwner) {
            feedAdapter.submitList(it)
        }
    }

    private fun setupRecyclerView() {
        feed.layoutManager = GridLayoutManager(requireContext(), 2)
        feed.adapter = feedAdapter
    }

    private fun setupEndlessScrollListener() {
        endlessScrollListener = EndlessScrollListener {
            feedViewModel.loadMoreItems()
        }
        feed.addOnScrollListener(endlessScrollListener)
    }

    fun onItemClick(item: CatalogItem) {
        Log.d("Test", item.toString())
        val action = FeedFragmentDirections.actionFeedFragmentToItemDetailsFragment(currentItem = item)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        feed.removeOnScrollListener(endlessScrollListener)
    }
}
