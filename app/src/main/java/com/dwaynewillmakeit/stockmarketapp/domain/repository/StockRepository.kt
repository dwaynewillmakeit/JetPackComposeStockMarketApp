package com.dwaynewillmakeit.stockmarketapp.domain.repository

import com.dwaynewillmakeit.stockmarketapp.domain.model.CompanyListing
import com.dwaynewillmakeit.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>>
}