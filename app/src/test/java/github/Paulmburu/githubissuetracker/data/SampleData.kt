package github.Paulmburu.githubissuetracker.data

import github.Paulmburu.githubissuetracker.data.models.UserIssue

object SampleData {

    const val LOGIN = "ndagistanley"

    val issue1 = UserIssue(
        "MDU6SXNzdWU0OTk1NDQwNjk=",
        "gcpdemo",
        "OPEN",
        "0",
        "2019-09-27T16:43:37Z",
        "@jmwai This could do...?\\n\\n\\nInstall Google Cloud SDK\\nbrew install google-cloud-sdk (using Brew for Mac OS and Linux) OR follow this guide.\\n\\n\\nLogin - gcloud auth login then select the gmail account that has GCP set up and billing enabled.\\n\\n\\nSet project - gcloud config set project [PROJECT-ID]"
    )
    val issue2 = UserIssue(
        "MDU6SXNzdWUzNjI5NzM2Njg=",
        "rafiki-zsh",
        "OPEN",
        "0",
        "2018-09-23T20:15:24Z",
        "I'm glad to inform us that this repo has been added to the libraries list in github.andela.com."
    )

    val issue3 = UserIssue(
        "MDU6SXNzdWUzNjIyMDUwMzM=",
        "rafiki-zsh",
        "CLOSED",
        "2",
        "2018-09-20T14:06:55Z",
        "The rafiki picture is missing"
    )

    val issue4 = UserIssue(
        "MDU6SXNzdWUzMzc3NDE3NzU=",
        "vue-django",
        "OPEN",
        "2",
        "2018-07-03T04:29:39Z",
        "Enable automatic deployment to heroku"
    )

    val issue5 = UserIssue(
        "MDU6SXNzdWUzMzc3NDA3NzQ=",
        "vue-django",
        "CLOSED",
        "0",
        "2018-07-03T04:22:30Z",
        "Rebase branches v1-d1 and v2-d1 with develop branch"
    )

    val userIssues = arrayListOf(issue1, issue2, issue3, issue4, issue5)
}