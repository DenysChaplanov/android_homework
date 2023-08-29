package com.vinted.demovinted.ui.feed

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.vinted.demovinted.R
import com.vinted.demovinted.data.models.CatalogItem
import com.vinted.demovinted.data.models.ItemBoxViewEntity
import com.vinted.demovinted.ui.details.ItemDetailsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_feed.*


//@AndroidEntryPoint
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
        //val itemBoxViewEntity = ItemBoxViewEntity.fromCatalogItem(Item)
        Log.d("Test2", Item.toString())
        val action = FeedFragmentDirections.actionFeedFragmentToItemDetailsFragment(currentItem = Item)
        findNavController().navigate(action)
        //parentFragmentManager.commit { add((ItemDetailsFragment()), "S" )}
    }

}
