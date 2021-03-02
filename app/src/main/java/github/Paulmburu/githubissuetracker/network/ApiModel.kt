package github.Paulmburu.githubissuetracker.network

/**
 * A wrapper class to hold a model type with its given API status (i.e. LOADING, ERROR, SUCCESS).
 */
data class ApiModel<out T>(
    val status: ApiRequestStatus,
    val data: T?,
    val apiFailure: ApiFailure?
) {
    companion object {
        fun <T> success(data: T): ApiModel<T> = ApiModel(ApiRequestStatus.SUCCESS, data, null)
        fun <T> failure(apiFailure: ApiFailure, data: T?): ApiModel<T> =
            ApiModel(ApiRequestStatus.FAILURE, data, apiFailure)
        fun <T> loading(data: T?): ApiModel<T> = ApiModel(ApiRequestStatus.LOADING, data, null)
    }
}