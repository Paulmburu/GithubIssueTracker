package github.Paulmburu.githubissuetracker

import com.apollographql.apollo.ApolloClient
import github.Paulmburu.githubissuetracker.utils.AuthInterceptor
import github.Paulmburu.githubissuetracker.utils.Constants
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

val authInterceptor = AuthInterceptor()

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(authInterceptor)
    .connectTimeout(15, TimeUnit.SECONDS)
    .readTimeout(15, TimeUnit.SECONDS)
    .build()

val apolloClient = ApolloClient.builder()
    .serverUrl(Constants.GRAPHQL_BASE_URL)
    .okHttpClient(okHttpClient)
    .build()

