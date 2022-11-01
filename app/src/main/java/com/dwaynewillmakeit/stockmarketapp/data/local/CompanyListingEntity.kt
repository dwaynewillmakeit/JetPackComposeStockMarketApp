package com.dwaynewillmakeit.stockmarketapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company_listing")
data class CompanyListingEntity(
    val name: String,
    val symbol: String,
    val exchange: String,
    @PrimaryKey val id: Int? = null
)