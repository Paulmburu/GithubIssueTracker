package github.Paulmburu.githubissuetracker.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import github.Paulmburu.githubissuetracker.R
import github.Paulmburu.githubissuetracker.data.models.UserIssue
import github.Paulmburu.githubissuetracker.databinding.RowGithubIssueBinding
import github.Paulmburu.githubissuetracker.utils.convertDate
import github.Paulmburu.githubissuetracker.utils.convertDateUsingLegacyClasses
import github.Paulmburu.githubissuetracker.utils.validateIfEmpty

class TrackIssuesAdapter(
    private val context: Context,
    val issues: List<UserIssue>
) :
    RecyclerView.Adapter<TrackIssuesAdapter.ViewHolder>() {

    class ViewHolder(val binding: RowGithubIssueBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return issues.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RowGithubIssueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val issues = issues.get(position)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.binding.issueCreationDateTextView.text = issues.createdAt.trim().convertDate()
        } else {
            holder.binding.issueCreationDateTextView.text =
                issues.createdAt.trim().convertDateUsingLegacyClasses()
        }

        issues.state.run {
            holder.binding.issueStateChip.text = this
            if (this.equals("OPEN"))
                holder.binding.issueStateChip.setChipBackgroundColorResource(R.color.scarlet)
            else if(this.equals("CLOSED"))
                holder.binding.issueStateChip.setChipBackgroundColorResource(R.color.purple_500)
        }


        holder.binding.issueBodyTextView.text = issues.bodyText.validateIfEmpty()
        holder.binding.repositoryTextView.text = issues.repositoryName
        holder.binding.totalCommentsTextView.text = issues.commentsCount
    }


}