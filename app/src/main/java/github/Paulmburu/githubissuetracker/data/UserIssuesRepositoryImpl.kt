package github.Paulmburu.githubissuetracker.data

import TrackUserIssuesQuery
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import github.Paulmburu.githubissuetracker.data.models.UserIssue
import github.Paulmburu.githubissuetracker.network.ApiFailure
import github.Paulmburu.githubissuetracker.network.data
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface UserIssuesRepository {
    suspend fun fetchUserIssues(
        login: String,
        onFailure: (ApiFailure) -> Unit
    ): Flow<List<UserIssue>?>
}

class UserIssuesRepositoryImpl(private val apolloClient: ApolloClient) : UserIssuesRepository {
    override suspend fun fetchUserIssues(
        login: String,
        onFailure: (ApiFailure) -> Unit
    ): Flow<List<UserIssue>?> {
        val issuesData = arrayListOf<UserIssue>()
        val response = apolloClient.query(TrackUserIssuesQuery(login)).toDeferred().data(onFailure)
            ?: return flowOf()
        return flowOf(
            response.let {
                when {
                    it.user() != null -> {
                        it.user()?.issues()?.edges()?.forEach {
                            issuesData.add(UserIssue(it.node()))
                        }
                        issuesData
                    }
                    else -> null
                }
            }
        )

    }
}