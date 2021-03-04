package github.Paulmburu.githubissuetracker.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import github.Paulmburu.githubissuetracker.R
import github.Paulmburu.githubissuetracker.data.FakeAndroidUsserIssuesRespository
import github.Paulmburu.githubissuetracker.data.UserIssuesRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.test.espresso.action.ViewActions.typeText

import android.widget.AutoCompleteTextView

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import github.Paulmburu.githubissuetracker.data.AndroidSampleData.LOGIN


@HiltAndroidTest
class MainActivityTest{

    private lateinit var repository: UserIssuesRepository

    @Before
    fun setUp() {
        repository = FakeAndroidUsserIssuesRespository()
        launchActivity()
    }

    @Test
    fun whenSearchItemIsClicked_shouldAllowTextInputAsSearchView(){

        onView(withId(R.id.search_item)).perform(click())
        onView(isAssignableFrom(AutoCompleteTextView::class.java)).perform(typeText(LOGIN))

    }

    @Test
    fun whenAppIsLaunched_searchIllustrationShouldBeVisible(){
        onView(withId(R.id.search_user_issues_image_view)).check(matches(isDisplayed()));
    }

    fun ViewInteraction.isGone() = getViewAssertion(ViewMatchers.Visibility.GONE)

    fun ViewInteraction.isVisible() = getViewAssertion(ViewMatchers.Visibility.VISIBLE)

    fun ViewInteraction.isInvisible() = getViewAssertion(ViewMatchers.Visibility.INVISIBLE)

    private fun getViewAssertion(visibility: ViewMatchers.Visibility): ViewAssertion? {
        return ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(visibility))
    }

    @Test
    fun whenAppIslaunched_emptySearchResultIllustrationShouldBeGone(){
        onView(withId(R.id.empty_result_image_view)).isGone()
    }

    @Test
    fun whenAppIsLaunched_toolbarShouldBeVisible(){
        onView(withId(R.id.main_toolbar)).isVisible()
    }


    private fun launchActivity(): ActivityScenario<MainActivity>? {
        return launch(MainActivity::class.java)
    }

}