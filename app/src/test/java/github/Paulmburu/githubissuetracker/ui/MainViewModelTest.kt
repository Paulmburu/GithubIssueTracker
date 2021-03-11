package github.Paulmburu.githubissuetracker.ui

import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.apollographql.apollo.api.Input
import com.google.common.truth.Truth.assertThat
import github.Paulmburu.githubissuetracker.BaseViewModelTest
import github.Paulmburu.githubissuetracker.R
import github.Paulmburu.githubissuetracker.data.FakeUsserIssuesRespository
import github.Paulmburu.githubissuetracker.data.SampleData.LOGIN
import github.Paulmburu.githubissuetracker.data.SampleData.issue1
import github.Paulmburu.githubissuetracker.data.SampleData.issue2
import github.Paulmburu.githubissuetracker.data.SampleData.issue3
import github.Paulmburu.githubissuetracker.getOrAwaitValue
import github.Paulmburu.githubissuetracker.network.ApiFailureType
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import type.OrderDirection


class MainViewModelTest : BaseViewModelTest() {

    // Subject under test
    private lateinit var viewModel: MainViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var repository: FakeUsserIssuesRespository

    @Before
    fun setupViewModel() {
        repository = FakeUsserIssuesRespository()
        viewModel = MainViewModel(repository)
    }

    @Test
    fun `given no issues exist, a faiure should occur`(){
        viewModel.fetchUserIssues(LOGIN, OrderDirection.DESC, Input.fromNullable(null))
        val error = viewModel.errorsLiveData.getOrAwaitValue {  }
//        assertThat(error).isEqualTo(R.string.response_error_string)
    }

    @Test
    fun `given success data fetch, all issues should be returned`(){
        repository.addIssues(issue1, issue2, issue3)

        viewModel.fetchUserIssues(LOGIN, OrderDirection.DESC, Input.fromNullable(null))
        val issues = viewModel.userIssuesLiveData.getOrAwaitValue {  }
        assertThat(issues).contains(issue2)
    }

}