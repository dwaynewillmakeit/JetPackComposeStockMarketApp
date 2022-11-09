package com.dwaynewillmakeit.stockmarketapp.di

import com.dwaynewillmakeit.stockmarketapp.data.csv.CSVParser
import com.dwaynewillmakeit.stockmarketapp.data.csv.CompanyListingsParser
import com.dwaynewillmakeit.stockmarketapp.data.repository.StockRepositoryImpl
import com.dwaynewillmakeit.stockmarketapp.domain.model.CompanyListing
import com.dwaynewillmakeit.stockmarketapp.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingsParse(companyListingsParser: CompanyListingsParser): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindsStockRepository(stockRepository: StockRepositoryImpl):StockRepository
}