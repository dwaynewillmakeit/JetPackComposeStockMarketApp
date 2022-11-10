package com.dwaynewillmakeit.stockmarketapp.ui.company_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dwaynewillmakeit.stockmarketapp.domain.repository.StockRepository
import com.dwaynewillmakeit.stockmarketapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@HiltViewModel
class CompanyInfoViewModel constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: StockRepository
) : ViewModel() {

    var state by mutableStateOf(CompanyInfoState())

    init{
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol")?: return@launch // if the symbol is null we don't want to continue.
            state = state.copy(isLoading = true)
            val companyInfoResult = async {repository.getCompanyInfo(symbol)}
            val intradayInfoResult = async {repository.getIntraDayInfo(symbol)  }

            when(val result = companyInfoResult.await()){
                is Resource.Success->{
                   state = state.copy(company = result.data, isLoading = false, error = null)
                }
                is Resource.Error->{
                    state = state.copy( company = null, isLoading = false, error = result.message)

                }
                else -> Unit
            }

            when(val result = intradayInfoResult.await()){
                is Resource.Success->{
                   state = state.copy(stockInfos = result.data?: emptyList(), isLoading = false, error = null)
                }
                is Resource.Error->{
                    state = state.copy( company = null, isLoading = false, error = result.message)

                }
                else -> Unit
            }
        }
    }
}