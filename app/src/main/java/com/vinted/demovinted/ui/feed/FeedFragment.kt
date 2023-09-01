package com.vinted.demovinted.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.vinted.demovinted.R
import com.vinted.demovinted.data.models.CatalogItem
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment: Fragment(R.layout.fragment_feed) {

    private val feedAdapter by lazy { FeedAdapter(::onItemClick, ::onItemViewed) }
    private val feedViewModel by viewModels<FeedViewModel>()

    private lateinit var endlessScrollListener: EndlessScrollListener
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupRecyclerView()
        setupEndlessScrollListener()

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

    private fun onItemClick(item: CatalogItem) {
        Log.d("Test", item.toString())
        val action = FeedFragmentDirections.actionFeedFragmentToItemDetailsFragment(currentItem = item)
        findNavController().navigate(action)
    }
    private fun onItemViewed(item: CatalogItem) {
        feedViewModel.sendItemViewEvent(item)
        Log.d("PostTest", item.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                feedViewModel.onSearch(newText.orEmpty())
                Log.d("NewTextSearch", newText.toString())
                return true
            }
        })
    }

}
