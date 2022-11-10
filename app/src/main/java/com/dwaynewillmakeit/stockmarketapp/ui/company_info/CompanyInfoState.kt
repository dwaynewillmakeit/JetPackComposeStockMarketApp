package com.dwaynewillmakeit.stockmarketapp.ui.company_info

import com.dwaynewillmakeit.stockmarketapp.domain.model.CompanyInfo
import com.dwaynewillmakeit.stockmarketapp.domain.model.IntradayInfo

data class CompanyInfoState(
    val stockInfos: List<IntradayInfo> = emptyList(),
    val company: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)