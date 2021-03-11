package github.Paulmburu.githubissuetracker.data


import com.apollographql.apollo.api.Input
import github.Paulmburu.githubissuetracker.data.AndroidSampleData.issue1
import github.Paulmburu.githubissuetracker.data.AndroidSampleData.issue2
import github.Paulmburu.githubissuetracker.data.AndroidSampleData.issue3
import github.Paulmburu.githubissuetracker.data.AndroidSampleData.issue4
import github.Paulmburu.githubissuetracker.data.AndroidSampleData.issue5
import github.Paulmburu.githubissuetracker.data.models.UserIssue
import github.Paulmburu.githubissuetracker.network.ApiFailure
import github.Paulmburu.githubissuetracker.network.ApiFailureType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import type.OrderDirection

class FakeAndroidUsserIssuesRespository : UserIssuesRepository{

    var issuesData: LinkedHashMap<String, UserIssue> = LinkedHashMap()

    init {
        addIssues(issue1, issue2, issue3, issue4,issue5)
    }

    override suspend fun fetchUserIssues(
        login: String,
        direction: OrderDirection,
        labels: Input<List<String?>?>,
        onFailure: (ApiFailure) -> Unit
    ): Flow<List<UserIssue>?> {
        if(issuesData.size == 0){
            onFailure(ApiFailure(ApiFailureType.RESPONSE_ERROR,"No Data Found", 404))
            return flowOf()
        }
        return flow { emit(issuesData.values.toList()) }
    }

    fun addIssues(vararg usserIssues: UserIssue) {
        for (usserIssue in usserIssues) {
            usserIssue.repositoryId?.let {
                issuesData[it] = usserIssue
            }

        }
    }

}