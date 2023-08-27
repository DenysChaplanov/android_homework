package com.vinted.demovinted.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vinted.demovinted.R
import com.vinted.demovinted.data.models.CatalogItem
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment: Fragment(R.layout.fragment_feed) {

    private val feedAdapter by lazy { FeedAdapter(::onItemClick) }
    private val feedViewModel by viewModels<FeedViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feed.layoutManager = LinearLayoutManager(requireContext())
        feed.adapter = feedAdapter
        feedViewModel.Test()
        feedViewModel.feedData.observe(viewLifecycleOwner){
            feedAdapter.submitList(it)
        }
    }
    fun onItemClick(Item: CatalogItem){
        Log.d("Test", Item.toString())
    }

}
