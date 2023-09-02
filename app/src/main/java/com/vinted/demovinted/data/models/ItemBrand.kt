package com.vinted.demovinted.data.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemBrand(
    val id: String = "",
    @Json(name = "is_custom_brand") val isCustomBrand: Boolean = false,
    @Json(name = "favourite_count")var favouriteCount: Int = 0,
    @Json(name = "item_count")val itemCount: Int = 0,
    @Json(name = "pretty_image_count")val prettyItemCount: String = "",
    val title: String = ""
) : Parcelable {

    override fun toString() = title

    companion object {
        const val NO_BRAND_ID = "1"

        @JvmOverloads
        @JvmStatic
        fun createNoBrand(title: String = "") = ItemBrand(NO_BRAND_ID, title = title)
    }
}
