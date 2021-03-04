package github.Paulmburu.githubissuetracker.di

import com.apollographql.apollo.ApolloClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import github.Paulmburu.githubissuetracker.data.UserIssuesRepository
import github.Paulmburu.githubissuetracker.data.UserIssuesRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providesUserIssuesRepository(
        apolloClient: ApolloClient
    ): UserIssuesRepository = UserIssuesRepositoryImpl(apolloClient)

}