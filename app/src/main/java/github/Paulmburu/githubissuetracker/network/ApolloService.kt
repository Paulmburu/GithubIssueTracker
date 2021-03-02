/*
 * Copyright 2020 Safeboda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package github.Paulmburu.githubissuetracker.network

import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.*
import kotlinx.coroutines.Deferred
import java.net.UnknownHostException

internal const val PAGE_SIZE = 100

private fun onApolloException(e: ApolloException): ApiFailure {
    return when (e) {
        is ApolloNetworkException ->
            if (e.cause is UnknownHostException) {
                ApiFailure(ApiFailureType.NO_NETWORK, null, null)
            } else {
                ApiFailure(ApiFailureType.HTTP_ERROR, null, null)
            }
        is ApolloHttpException -> when (val code = e.code()) {
            401 -> ApiFailure(ApiFailureType.UNAUTHORIZED, e.message(), code)
            in 500..599 -> ApiFailure(ApiFailureType.SERVER_ERROR, e.message(), code)
            else -> ApiFailure(ApiFailureType.HTTP_ERROR, e.message(), code)
        }
        is ApolloParseException -> ApiFailure(ApiFailureType.PARSE_ERROR, null, null)
        is ApolloCanceledException -> ApiFailure(ApiFailureType.CANCELED, null, null)
        else -> ApiFailure(ApiFailureType.UNKNOWN, null, null)
    }
}

private val <T> Response<T>.error: ApiFailure?
    get() {
        if (data == null || errors.orEmpty().isNotEmpty()) {
            return ApiFailure(
                ApiFailureType.RESPONSE_ERROR,
                errors?.firstOrNull()?.message,
                null
            )
        }
        return null
    }

suspend fun <T> Deferred<Response<T>>.data(onFailure: (ApiFailure) -> Unit): T? =
    try {
        val response = this.await()
        response.error?.let {
            onFailure(it)
            return null
        }
        response.data
    } catch (e: ApolloException) {
        onFailure(onApolloException(e))
        null
    }