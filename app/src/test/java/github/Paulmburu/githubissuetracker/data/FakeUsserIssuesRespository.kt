package github.Paulmburu.githubissuetracker.data

import com.apollographql.apollo.api.Input
import github.Paulmburu.githubissuetracker.data.models.UserIssue
import github.Paulmburu.githubissuetracker.network.ApiFailure
import github.Paulmburu.githubissuetracker.network.ApiFailureType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import type.OrderDirection

class FakeUsserIssuesRespository : UserIssuesRepository{

    var issuesData: LinkedHashMap<String, UserIssue> = LinkedHashMap()

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