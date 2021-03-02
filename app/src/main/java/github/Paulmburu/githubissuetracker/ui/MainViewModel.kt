package github.Paulmburu.githubissuetracker.ui

import androidx.lifecycle.*
import github.Paulmburu.githubissuetracker.data.UserIssuesRepository
import github.Paulmburu.githubissuetracker.data.models.UserIssues
import github.Paulmburu.githubissuetracker.network.ApiFailure
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(
    private val trackUserIssuesRepository: UserIssuesRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _userIssuesLiveData = MutableLiveData<List<UserIssues>>()
    val userIssuesLiveData: LiveData<List<UserIssues>>
        get() = _userIssuesLiveData

    private val _errorsLiveData = MutableLiveData<String>()
    val errorsLiveData: LiveData<String>
        get() = _errorsLiveData


    fun fetchUserIssues(login: String) {
        viewModelScope.launch(coroutineDispatcher) {
            trackUserIssuesRepository.fetchUserIssues(login) {
                viewModelScope.launch {
                    handleFetchFailure(it, login)
                }
            }.collect {
                _userIssuesLiveData.postValue(it)
            }
        }
    }

    private fun handleFetchFailure(
        failure: ApiFailure,
        login: String?
    ) {
        _errorsLiveData.postValue(failure.failureType.name)
    }
}

class MainViewFactory(
    private val trackUserIssuesRepository: UserIssuesRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = MainViewModel(trackUserIssuesRepository) as T
}