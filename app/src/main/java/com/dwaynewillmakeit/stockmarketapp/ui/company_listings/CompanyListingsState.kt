package com.dwaynewillmakeit.stockmarketapp.ui.company_listings

import com.dwaynewillmakeit.stockmarketapp.domain.model.CompanyListing

//Store the states needed for the company listing screen
data class CompanyListingsState(
    val companies: List<CompanyListing> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = ""
)
