package com.dwaynewillmakeit.stockmarketapp.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockAPI {

    @GET("query?function=LISTING_STATUS")
    suspend fun getListings(@Query("apikey")apiKer:String = API_KEY):ResponseBody

    companion object{
        const val API_KEY = "H0RVJCGVKAEUIBFC"
        const val BASE_URL = "https://alphavantage.co"
    }
}