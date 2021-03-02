package github.Paulmburu.githubissuetracker.ui

import TrackUserIssuesQuery
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.google.android.material.snackbar.Snackbar
import github.Paulmburu.githubissuetracker.R
import github.Paulmburu.githubissuetracker.adapters.TrackIssuesAdapter
import github.Paulmburu.githubissuetracker.apolloClient
import github.Paulmburu.githubissuetracker.data.UserIssuesRepository
import github.Paulmburu.githubissuetracker.data.models.UserIssues
import github.Paulmburu.githubissuetracker.utils.hideKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: TrackIssuesAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewFactory

    private lateinit var searchView: SearchView
    private lateinit var searchItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_toolbar.title = getString(R.string.menu_search)
        main_toolbar.inflateMenu(R.menu.menu_search)

        viewModelFactory = MainViewFactory(UserIssuesRepository(apolloClient))
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        observeViewModel()

        searchItem = main_toolbar.menu.findItem(R.id.search_item)
        searchView = searchItem.actionView as SearchView
        searchView.queryHint = resources.getString(R.string.menu_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.fetchUserIssues(query.toString())
                searchView.hideKeyboard()
                searchItem.collapseActionView()
                main_toolbar.title = query.toString()
                empty_result_image_view.isVisible = false
                issues_recyclerview.animate().alpha(0.0f)
                search_user_issues_image_view.animate().alpha(0.0f)
                progress_animation.animate().alpha(1.0f)
                return true
            }
        })
    }

    fun observeViewModel(){
        viewModel.userIssuesLiveData.observe(this, Observer {
            adapter = TrackIssuesAdapter(this, it)
            issues_recyclerview.adapter = adapter
            issues_recyclerview.animate().alpha(1.0f)
            progress_animation.animate().alpha(0.0f)
            empty_result_image_view.isVisible = false
            search_user_issues_image_view.animate().alpha(0.0f)


        })

        viewModel.errorsLiveData.observe(this, Observer {
            issues_recyclerview.animate().alpha(0.0f)
            progress_animation.animate().alpha(0.0f)
            search_user_issues_image_view.animate().alpha(0.0f)
            empty_result_image_view.isVisible = true

            Snackbar.make(
                    main_frame_layout,
                    it,
                    Snackbar.LENGTH_LONG
                ).show()
        })
    }
}