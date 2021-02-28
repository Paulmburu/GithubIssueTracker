package github.Paulmburu.githubissuetracker

import TrackUserIssuesQuery
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            try {
                val response = apolloClient.query(TrackUserIssuesQuery("jumaallan")).toDeferred().await()
                Log.d("GithubApi", "Success ${response?.data}")
            }catch (e: ApolloException) {
                Log.d("GithubApi", "Failure", e)
                null
            }


        }
    }
}