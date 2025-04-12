package com.namaya.oscarsthegrouch_app.ui.gamehome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namaya.oscarsthegrouch_app.databinding.PlayerCardBinding

class User(val name: String)

class Player(val user: User, val gameId: Int, val score: Int, val state: String = "Waiting")

class PlayerListViewAdapter(
    private val onItemClick: (Int) -> Unit
): RecyclerView.Adapter<PlayerListViewAdapter.ViewHolder>(){
    private val playersList = mutableListOf(
        Player(User("Player 1"), 0, 0),
        Player(User("Player 2"), 0, 0),
        Player(User("Player 3"), 0, 0)
    )

    inner class ViewHolder(val viewBinding: PlayerCardBinding): RecyclerView.ViewHolder(viewBinding.root)

    /**
     * Create a new view holder.
     *
     * This function is called by the runtime as part of constructing the views for the list. There
     * will only ever be a static number of view holders regardless of the size of the data list (when
     * list size > screen size).
     *
     * @param parent ?
     * @param viewType ?
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding = PlayerCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val viewHolder = ViewHolder(viewBinding)
        viewBinding.root.setOnClickListener {
            onItemClick(viewHolder.adapterPosition)
        }

        return ViewHolder(viewBinding)
    }

    /**
     * Get the size of the campaign list.
     *
     * @return The number of elements in the campaign list.
     */
    override fun getItemCount(): Int {
        return playersList.size
    }

    /**
     * Bind an element to a view holder.
     *
     * This function is called by the runtime when a new element needs to be displayed in the campaign list.
     *
     * @param holder The view holder to bind the new element to.
     * @param position The position of the element in the data list.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        require(position >= 0 && position < playersList.size) { "Invalid position in players list." }

        val cardViewBinding = holder.viewBinding

        cardViewBinding.playerName.text = playersList[position].user.name

        if (playersList[position].state == "Ready") {
            cardViewBinding.readyCheckmark.visibility = View.VISIBLE
        } else {
            cardViewBinding.readyCheckmark.visibility = View.GONE
        }

        cardViewBinding.playerScore.text = String.format("%s", playersList[position].score)
    }
}