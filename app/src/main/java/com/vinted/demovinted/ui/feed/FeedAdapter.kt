package com.vinted.demovinted.ui.feed

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vinted.demovinted.R
import com.vinted.demovinted.data.models.CatalogItem
import com.vinted.demovinted.ui.details.ItemDetailsFragment
import kotlinx.android.synthetic.main.feed_item.view.*

class FeedAdapter(val onItemClick :(CatalogItem) -> Unit, diff: DiffUtil.ItemCallback<CatalogItem> = DefaultFeedItemCallBack()): ListAdapter <CatalogItem, FeedViewHolder>(diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view =
        LayoutInflater.from(parent.context).inflate(R.layout.feed_item, parent, false)
        return FeedViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        getItem(position)?.let { holder.Bind(it) }
    }
}

class FeedViewHolder(view: View, val onItemClick :(CatalogItem) -> Unit): RecyclerView.ViewHolder(view){
    fun Bind(Item: CatalogItem){
        itemView.brand.text = Item.itemBrand.toString()
        Glide.with(itemView.context).load(Item.mainPhoto.url).into(itemView.image)
        itemView.setOnClickListener{
            Log.d("Test", "Bind: ")
            onItemClick(Item)
        }
    }
}

class DefaultFeedItemCallBack: DiffUtil.ItemCallback<CatalogItem> (){

    override fun areItemsTheSame(oldItem: CatalogItem, newItem: CatalogItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CatalogItem, newItem: CatalogItem): Boolean {
        return oldItem == newItem
    }
}