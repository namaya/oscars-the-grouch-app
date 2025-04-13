package com.namaya.oscarsthegrouch_app.ui.gamehome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namaya.oscarsthegrouch_app.databinding.PlayerCardBinding
import com.namaya.oscarsthegrouch_app.model.Player
import com.namaya.oscarsthegrouch_app.model.User

class PlayerListViewAdapter(
    private val onItemClick: (Player) -> Unit
): RecyclerView.Adapter<PlayerListViewAdapter.ViewHolder>(){
    // TODO: set this on the viewmodel
    private val playersList = mutableListOf(
        Player(User("Player 1"), 0, 0),
        Player(User("Player 2"), 0, 0),
        Player(User("Player 3"), 0, 0)
    )

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

    override fun getItemCount(): Int {
        return playersList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        require(position >= 0 && position < playersList.size) { "Invalid position in players list." }
        holder.bind(playersList[position])
    }
}