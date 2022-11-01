package com.dwaynewillmakeit.stockmarketapp.data.mapper

import com.dwaynewillmakeit.stockmarketapp.data.local.CompanyListingEntity
import com.dwaynewillmakeit.stockmarketapp.domain.model.CompanyListing

//This is called an extension function
fun CompanyListingEntity.toCompanyListing():CompanyListing{
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity():CompanyListingEntity{
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}