package github.Paulmburu.githubissuetracker.network

/**
 * Wrapper class to hold http request failure data.
 */
data class ApiFailure(val failureType: ApiFailureType, val message: String?, val code: Int?) {
    val showTryAgain
        get() = (failureType != ApiFailureType.RESPONSE_ERROR) && (code == null || code < 400 || code > 499)
}