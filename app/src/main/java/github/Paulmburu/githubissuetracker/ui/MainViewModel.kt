package github.Paulmburu.githubissuetracker.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.apollographql.apollo.api.Input
import github.Paulmburu.githubissuetracker.data.UserIssuesRepository
import github.Paulmburu.githubissuetracker.data.UserIssuesRepositoryImpl
import github.Paulmburu.githubissuetracker.data.models.UserIssue
import github.Paulmburu.githubissuetracker.di.NetworkModule
import github.Paulmburu.githubissuetracker.network.ApiFailure
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
        _errorsLiveData.postValue(failure.failureType.name)
    }

    fun toggleDirectionFilter() {
        if (_directionFilter.value == OrderDirection.ASC)
            _directionFilter.value = OrderDirection.DESC
        else if (_directionFilter.value == null)
            _directionFilter.value = OrderDirection.DESC
        else _directionFilter.value = OrderDirection.ASC
    }
}