package com.dwaynewillmakeit.stockmarketapp.ui.company_listings

import android.util.Log
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dwaynewillmakeit.stockmarketapp.domain.repository.StockRepository
import com.dwaynewillmakeit.stockmarketapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyListViewModel @Inject constructor(private val repository: StockRepository) :
    ViewModel() {

    var state by mutableStateOf(CompanyListingsState())

    private var searchJob: Job? = null;

    init {
        getCompanyListings()
    }

    fun onEvent(event: CompanyListingEvent) {
        when (event) {
            is CompanyListingEvent.Refresh -> {
                getCompanyListings(fetchFromRemote = true)
            }
            is CompanyListingEvent.OnSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)

                searchJob?.cancel()

                searchJob = viewModelScope.launch {
                    //The delay is to prevent this coroutine from running as soon as the user types a letter.
                    delay(500L)
                    getCompanyListings()
                }
            }
        }
    }

  private  fun getCompanyListings(
        query: String = state.searchQuery.lowercase(),
        fetchFromRemote: Boolean = false
    ) {
        viewModelScope.launch {
            repository.getCompanyListings(fetchFromRemote, query).collect { result ->

                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { listings ->

                                    state = state.copy(
                                        companies = listings
                                    )
                                
                            }
                        }
                        is Resource.Error -> {
                            Log.i("TAG", "getCompanyListings: "+result.message)
                        }
                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                    }

            }
        }
    }


}