package com.vinted.demovinted.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vinted.demovinted.R
import com.vinted.demovinted.data.models.CatalogItem
import kotlinx.android.synthetic.main.feed_item.*
import kotlinx.android.synthetic.main.feed_item.view.*
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment: Fragment(R.layout.fragment_feed) {

    private val feedAdapter by lazy { FeedAdapter(::onItemClick) }
    private val feedViewModel by viewModels<FeedViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feed.layoutManager = GridLayoutManager(requireContext(), 2)
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
