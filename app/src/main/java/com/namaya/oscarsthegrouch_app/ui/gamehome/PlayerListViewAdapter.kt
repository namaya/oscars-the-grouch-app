package com.namaya.oscarsthegrouch_app.ui.gamehome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.namaya.oscarsthegrouch_app.databinding.PlayerCardBinding
import com.namaya.oscarsthegrouch_app.model.Player
import com.namaya.oscarsthegrouch_app.model.User

class PlayerListViewAdapter(
    private val onItemClick: (Player) -> Unit
): ListAdapter<Player, PlayerListViewAdapter.ViewHolder>(ItemDiffCallback) {
    inner class ViewHolder(private val viewBinding: PlayerCardBinding): RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: Player) {
            viewBinding.playerName.text = item.user.name
            viewBinding.playerScore.text = String.format("%s", item.score)
            viewBinding.readyCheckmark.visibility = if (item.state == "Ready") View.VISIBLE else View.GONE

            viewBinding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding = PlayerCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    object ItemDiffCallback : DiffUtil.ItemCallback<Player>() {
        override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem == newItem
        }
    }
}