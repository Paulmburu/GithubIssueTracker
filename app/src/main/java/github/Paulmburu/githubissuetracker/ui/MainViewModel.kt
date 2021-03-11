package github.Paulmburu.githubissuetracker.ui

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.apollographql.apollo.api.Input
import dagger.hilt.android.qualifiers.ApplicationContext
import github.Paulmburu.githubissuetracker.R
import github.Paulmburu.githubissuetracker.data.UserIssuesRepository
import github.Paulmburu.githubissuetracker.data.models.UserIssue
import github.Paulmburu.githubissuetracker.di.NetworkModule
import github.Paulmburu.githubissuetracker.network.ApiFailure
import github.Paulmburu.githubissuetracker.network.ApiFailureType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import type.OrderDirection

class MainViewModel @ViewModelInject constructor(
    private val trackUserIssuesRepository: UserIssuesRepository,
    @NetworkModule.IoDispatcher private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _userIssuesLiveData = MutableLiveData<List<UserIssue>>()
    val userIssuesLiveData: LiveData<List<UserIssue>>
        get() = _userIssuesLiveData

    private val _errorsLiveData = MutableLiveData<String>()
    val errorsLiveData: LiveData<String>
        get() = _errorsLiveData

    private val _directionFilter = MutableLiveData<OrderDirection>(null)
    val directionFilter: LiveData<OrderDirection>
        get() = _directionFilter

    private val _checkedChipId = MutableLiveData<Int>(null)
    val checkedChipId: LiveData<Int>
        get() = _checkedChipId


    fun fetchUserIssues(
        login: String,
        direction: OrderDirection,
        labels: Input<List<String?>?>
    ) {
        viewModelScope.launch(coroutineDispatcher) {
            trackUserIssuesRepository.fetchUserIssues(login, direction, labels) {
                viewModelScope.launch {
                    handleFetchFailure(it)
                }
            }.collect {
                _userIssuesLiveData.postValue(it)
            }
        }
    }

    private fun handleFetchFailure(
        failure: ApiFailure
    ) {
        when(failure.failureType){
            ApiFailureType.NO_NETWORK -> displayError("No network connection")
            ApiFailureType.RESPONSE_ERROR -> displayError("Error ! Please confirm the github username is correct")
            ApiFailureType.HTTP_ERROR -> displayError("HTTP error")
            ApiFailureType.SERVER_ERROR -> displayError("Server error")
            ApiFailureType.PARSE_ERROR -> displayError("Request failed to parse")
            ApiFailureType.CANCELED -> displayError("Request Canceled")
            ApiFailureType.UNAUTHORIZED -> displayError("UNAUTHORIZED")
            ApiFailureType.UNKNOWN-> displayError("Unexpected Error occured, Please Try Again")
        }

    }

    private fun displayError(error: String) = _errorsLiveData.postValue(error)

    fun toggleDirectionFilter() {
        if (_directionFilter.value == OrderDirection.ASC)
            _directionFilter.value = OrderDirection.DESC
        else if (_directionFilter.value == null)
            _directionFilter.value = OrderDirection.DESC

        else _directionFilter.value = OrderDirection.ASC
    }

    fun setCheckChipId(id: Int){
        _checkedChipId.value = id
    }
}