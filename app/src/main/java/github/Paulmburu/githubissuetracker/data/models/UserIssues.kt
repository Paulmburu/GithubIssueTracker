package github.Paulmburu.githubissuetracker.data.models

import TrackUserIssuesQuery

data class UserIssues(
    val repositoryId: String?,
    val repositoryName: String?,
    val state: String?,
    val commentsCount: String,
    val createdAt: String,
    val bodyText: String
) {
    constructor(node: TrackUserIssuesQuery.Node?) : this(
        node?.repository()?.id(),
        node?.repository()?.name(),
        node?.state()?.name,
        node?.comments()?.totalCount().toString(),
        node?.createdAt().toString(),
        node?.bodyText().toString()
    )
}