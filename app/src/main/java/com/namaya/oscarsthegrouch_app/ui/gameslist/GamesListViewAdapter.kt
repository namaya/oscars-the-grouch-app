package com.namaya.oscarsthegrouch_app.ui.gameslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.namaya.oscarsthegrouch_app.databinding.GameCardBinding
import com.namaya.oscarsthegrouch_app.model.Game

class GamesListViewAdapter(
    private val onItemClick: (Game) -> Unit
): ListAdapter<Game, GamesListViewAdapter.ViewHolder>(GameDiffCallback) {
    inner class ViewHolder(private val viewBinding: GameCardBinding): RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: Game) {
            viewBinding.gameCardName.text = item.name

            viewBinding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding = GameCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    // TODO: make this robust
    object GameDiffCallback : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.name == newItem.name
        }
    }
}