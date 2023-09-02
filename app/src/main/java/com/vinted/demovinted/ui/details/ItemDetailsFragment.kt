package com.vinted.demovinted.ui.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderViewAdapter
import com.vinted.demovinted.R
import com.vinted.demovinted.data.models.ItemBoxViewEntity
import kotlinx.android.synthetic.main.fragment_feed.view.*
import kotlinx.android.synthetic.main.fragment_item_details.*

class ItemDetailsFragment : Fragment(R.layout.fragment_item_details) {


    private val args: ItemDetailsFragmentArgs by navArgs()
    private val item: ItemBoxViewEntity get() = ItemBoxViewEntity.fromCatalogItem(args.currentItem)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ItemDetailsFragment", "Item: $item")
        image_carousel.setSliderAdapter(SimpleSliderAdapter(listOf(item.mainPhoto!!.url)), false)
        image_carousel.setIndicatorAnimation(IndicatorAnimationType.SLIDE)
        setItem()
    }

    private fun setItem() {
        item_category.text = item.category
        item_price.text = "${item.price?.setScale(2)} €"
        item_brand.text = item.brandTitle
        item.size?.let { item_size.text = it } ?: run {
            item_size.visibility = View.GONE
            item_size_title.visibility = View.GONE
        }
    }

    inner class SimpleSliderAdapter(private val photos: List<String>): SliderViewAdapter<SimpleSliderAdapter.SimpleViewHolder>() {

        inner class SimpleViewHolder(view: View): SliderViewAdapter.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup?): SimpleViewHolder {
            val view = LayoutInflater.from(parent!!.context).inflate(R.layout.fragment_feed, parent, false)
            return SimpleViewHolder(view)
        }

        override fun getCount(): Int = photos.size

        override fun onBindViewHolder(viewHolder: SimpleViewHolder?, position: Int) {
            val imageView = viewHolder?.itemView?.image!!
            Glide.with(imageView).load(photos[position]).into(imageView)
        }
    }

    companion object {
        private const val currentItem = "currentItem"
        fun newInstance(item: ItemBoxViewEntity): ItemDetailsFragment {
            return ItemDetailsFragment().apply {
                arguments = Bundle().apply { putParcelable(currentItem, item) }
            }
        }
    }
}
