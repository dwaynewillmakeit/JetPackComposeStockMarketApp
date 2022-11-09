package com.dwaynewillmakeit.stockmarketapp.data.mapper

import com.dwaynewillmakeit.stockmarketapp.data.local.CompanyListingEntity
import com.dwaynewillmakeit.stockmarketapp.data.remote.dto.CompanyInfoDTO
import com.dwaynewillmakeit.stockmarketapp.domain.model.CompanyInfo
import com.dwaynewillmakeit.stockmarketapp.domain.model.CompanyListing

//This is called an extension function
fun CompanyListingEntity.toCompanyListing(): CompanyListing {
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyInfoDTO.toCompanyInfo(): CompanyInfo {

    return CompanyInfo(
        symbol = symbol ?: "",
        description = description ?: "",
        name = name ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )
}