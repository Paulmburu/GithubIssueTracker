package github.Paulmburu.githubissuetracker.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import github.Paulmburu.githubissuetracker.data.UserIssuesRepository
import github.Paulmburu.githubissuetracker.data.UserIssuesRepositoryImpl
import github.Paulmburu.githubissuetracker.data.models.UserIssue
import github.Paulmburu.githubissuetracker.di.NetworkModule
import github.Paulmburu.githubissuetracker.network.ApiFailure
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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


    fun fetchUserIssues(login: String) {
        viewModelScope.launch(coroutineDispatcher) {
            trackUserIssuesRepository.fetchUserIssues(login) {
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
}

class MainViewFactory(
    private val trackUserIssuesRepository: UserIssuesRepositoryImpl
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = MainViewModel(trackUserIssuesRepository) as T
}