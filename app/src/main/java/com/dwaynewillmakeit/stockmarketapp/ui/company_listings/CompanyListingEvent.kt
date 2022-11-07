package com.dwaynewillmakeit.stockmarketapp.ui.company_listings

sealed class CompanyListingEvent{
    object Refresh: CompanyListingEvent()
    data class  OnSearchQueryChange(val query: String): CompanyListingEvent()
}
