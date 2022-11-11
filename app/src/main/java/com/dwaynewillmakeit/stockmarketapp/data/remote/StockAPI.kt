package com.dwaynewillmakeit.stockmarketapp.data.remote

import com.dwaynewillmakeit.stockmarketapp.data.remote.dto.CompanyInfoDTO
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockAPI {

    @GET("query?function=LISTING_STATUS")
    suspend fun getListings(@Query("apikey") apiKer: String = API_KEY): ResponseBody

    @GET("query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv")
    suspend fun getIntradayInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKer: String = API_KEY
    ): ResponseBody

    @GET("query?function=OVERVIEW")
    suspend fun getCompanyInfo(
        @Query("symbol") symbol: String,
        @Query("apikey") apiKer: String = API_KEY
    ):CompanyInfoDTO

    companion object {
        const val API_KEY = "H0RVJCGVKAEUIBFC"
        const val BASE_URL = "https://alphavantage.co"
    }
}