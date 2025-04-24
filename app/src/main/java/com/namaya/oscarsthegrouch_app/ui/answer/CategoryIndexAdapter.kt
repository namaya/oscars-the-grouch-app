package com.namaya.oscarsthegrouch_app.ui.answer

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.namaya.oscarsthegrouch_app.R
import java.util.Locale

data class CategoryIndexItem(val index: Int, val isAnswered: Boolean)

class CategoryIndexAdapter(
    private val onItemClick: (Int) -> Unit
) : ListAdapter<CategoryIndexItem, CategoryIndexAdapter.IndexViewHolder>(CategoryIndexDiffCallback) {
    inner class IndexViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndexViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_index, parent, false) as TextView

        return IndexViewHolder(textView)
    }

    override fun onBindViewHolder(holder: IndexViewHolder, position: Int) {
        holder.textView.text = String.format(Locale.getDefault(), "%d", (position + 1))

        holder.textView.setBackgroundResource(
            if (getItem(position).isAnswered) R.drawable.bg_index_answered else R.drawable.bg_index_unanswered
        )

        holder.textView.setOnClickListener {
            onItemClick(position)
        }
    }

    object CategoryIndexDiffCallback : DiffUtil.ItemCallback<CategoryIndexItem>() {
        override fun areItemsTheSame(oldItem: CategoryIndexItem, newItem: CategoryIndexItem): Boolean {
            return oldItem.index == newItem.index && oldItem.isAnswered == newItem.isAnswered
        }

        override fun areContentsTheSame(oldItem: CategoryIndexItem, newItem: CategoryIndexItem): Boolean {
            return oldItem.index == newItem.index && oldItem.isAnswered == newItem.isAnswered
        }
    }
}