package github.Paulmburu.githubissuetracker.data

import TrackUserIssuesQuery
import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import github.Paulmburu.githubissuetracker.data.models.UserIssues
import github.Paulmburu.githubissuetracker.network.ApiFailure
import github.Paulmburu.githubissuetracker.network.data
import kotlinx.coroutines.flow.flowOf

class UserIssuesRepository(private val apolloClient: ApolloClient) {
    suspend fun fetchUserIssues(
        login: String,
        onFailure: (ApiFailure) -> Unit
    ): kotlinx.coroutines.flow.Flow<ArrayList<UserIssues>?> {
        val issuesData = arrayListOf<UserIssues>()
        val response = apolloClient.query(TrackUserIssuesQuery(login)).toDeferred().data(onFailure)
            ?: return flowOf()
        return flowOf(
            response.let {
                when {
                    it.user() != null -> {
                        it.user()?.issues()?.edges()?.forEach {
                            issuesData.add(UserIssues(it.node()))
                        }
                        issuesData
                    }
                    else -> null
                }
            }
        )

    }
}