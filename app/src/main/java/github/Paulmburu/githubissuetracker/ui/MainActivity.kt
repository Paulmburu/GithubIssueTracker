package github.Paulmburu.githubissuetracker.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.apollographql.apollo.api.Input
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import github.Paulmburu.githubissuetracker.R
import github.Paulmburu.githubissuetracker.adapters.TrackIssuesAdapter
import github.Paulmburu.githubissuetracker.utils.hideKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import type.OrderDirection

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var adapter: TrackIssuesAdapter
    private lateinit var searchView: SearchView
    private lateinit var searchItem: MenuItem
    private lateinit var filterItem: MenuItem
    private lateinit var orderDirectionType: OrderDirection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        main_toolbar.title = getString(R.string.menu_search)
        main_toolbar.inflateMenu(R.menu.menu_search)
        orderDirectionType = OrderDirection.ASC
        observeCheckedChips()
        observeViewModel()

        searchItem = main_toolbar.menu.findItem(R.id.search_item)
        searchView = searchItem.actionView as SearchView
        searchView.queryHint = resources.getString(R.string.menu_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                fetchData(query.toString(), Input.fromNullable(null))
                searchView.hideKeyboard()
                searchItem.collapseActionView()
                main_toolbar.title = query.toString()
                return true
            }
        })

        filterItem = main_toolbar.menu.findItem(R.id.filter_item)
        filterItem.setOnMenuItemClickListener {
            viewModel.toggleDirectionFilter()
            true
        }
    }

    fun observeCheckedChips() {
        main_chip_group.setOnCheckedChangeListener { group, checkedId ->
            Log.d(
                "TraChipGroup",
                "observeCheckedChips: ${group.findViewById<Chip>(checkedId).text}"
            )
        }
    }

    fun observeViewModel() {
        viewModel.userIssuesLiveData.observe(this, Observer {
            adapter = TrackIssuesAdapter(this, it)
            issues_recyclerview.adapter = adapter
            animateViews(1.0f,
                0.0f,
                0.0f,
                false)


        })

        viewModel.errorsLiveData.observe(this, Observer {
            animateViews(0.0f,
                0.0f,
                0.0f,
                true)

            Snackbar.make(
                main_frame_layout,
                it,
                Snackbar.LENGTH_LONG
            ).show()
        })

        viewModel.directionFilter.observe(this, Observer {
            if(it != null) {
                orderDirectionType = it
                var login = main_toolbar.title
                if(!login.equals(getString(R.string.menu_search))) {
                    fetchData(login.toString(), Input.fromNullable(null))
                }
            }
        })
    }

    fun animateViews(
        recyclerViewValue: Float,
        progressAnimationValue: Float,
        searchUserIssuesImageViewValue: Float,
        emptyResultImageViewValue: Boolean
    ) {
        issues_recyclerview.animate().alpha(recyclerViewValue)
        progress_animation.animate().alpha(progressAnimationValue)
        search_user_issues_image_view.animate().alpha(searchUserIssuesImageViewValue)
        empty_result_image_view.isVisible = emptyResultImageViewValue
    }

    fun fetchData(login: String, labels: Input<List<String?>?>){
        viewModel.fetchUserIssues(
            login,
            orderDirectionType,
            labels
        )
        animateViews(
            0.0f,
            1.0f,
            0.0f,
            false
        )
    }
}