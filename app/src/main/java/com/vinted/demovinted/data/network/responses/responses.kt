//response from the remote server to the request
package com.vinted.demovinted.data.network.responses

import com.vinted.demovinted.data.models.CatalogItem

open class CatalogItemListResponse(
    val items: List<CatalogItem> = emptyList(),
)