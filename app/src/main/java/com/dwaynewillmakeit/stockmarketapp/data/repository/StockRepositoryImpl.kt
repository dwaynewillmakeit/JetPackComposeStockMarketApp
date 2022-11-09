package com.dwaynewillmakeit.stockmarketapp.data.repository

import com.dwaynewillmakeit.stockmarketapp.data.csv.CSVParser
import com.dwaynewillmakeit.stockmarketapp.data.local.StockDatabase
import com.dwaynewillmakeit.stockmarketapp.data.mapper.toCompanyInfo
import com.dwaynewillmakeit.stockmarketapp.data.mapper.toCompanyListing
import com.dwaynewillmakeit.stockmarketapp.data.mapper.toCompanyListingEntity
import com.dwaynewillmakeit.stockmarketapp.data.remote.StockAPI
import com.dwaynewillmakeit.stockmarketapp.domain.model.CompanyInfo
import com.dwaynewillmakeit.stockmarketapp.domain.model.CompanyListing
import com.dwaynewillmakeit.stockmarketapp.domain.model.IntradayInfo
import com.dwaynewillmakeit.stockmarketapp.domain.repository.StockRepository
import com.dwaynewillmakeit.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockAPI,
    private val db: StockDatabase,
    private val companyListingsParser: CSVParser<CompanyListing>,
    private val intradayInfoParser: CSVParser<IntradayInfo>
) : StockRepository {

    private val dao = db.doa

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            //Set loading to true first thing so that the ui can show a loading bar
            emit(Resource.Loading(true))

            //Next fetch from the DB
            val localListing = dao.searchCompanyListing(query)

            //DB Will always be successful so we return a Success Resource object
            emit(Resource.Success(data = localListing.map { it.toCompanyListing() }))

            val isDBEmpty = localListing.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDBEmpty && !fetchFromRemote

            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))

                return@flow
            }

            val remoteListing = try {

                val response = api.getListings()

                companyListingsParser.parse(response.byteStream())


            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            }

            remoteListing?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListing(listings.map { it.toCompanyListingEntity() })
                emit(Resource.Success(dao.searchCompanyListing("").map { it.toCompanyListing() }))
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getIntraDayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {

            val response = api.getIntradayInfo(symbol)
            val results = intradayInfoParser.parse(response.byteStream())
            Resource.Success(results)

        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday info"
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday info"
            )
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val result = api.getCompanyInfo(symbol)
            Resource.Success(result.toCompanyInfo())
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday info"
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't load intraday info"
            )

        }
    }

}