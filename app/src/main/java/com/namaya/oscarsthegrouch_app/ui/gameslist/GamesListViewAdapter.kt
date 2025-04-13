package com.namaya.oscarsthegrouch_app.ui.gameslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.namaya.oscarsthegrouch_app.databinding.GameCardBinding
import com.namaya.oscarsthegrouch_app.model.Game

class GamesListViewAdapter: RecyclerView.Adapter<GamesListViewAdapter.ViewHolder>(){
    private val gamesList = mutableListOf(
        Game("Game 1"),
        Game("Game 2"),
    )

    inner class ViewHolder(val viewBinding: GameCardBinding): RecyclerView.ViewHolder(viewBinding.root)

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
        val viewBinding = GameCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)
    }

    /**
     * Get the size of the campaign list.
     *
     * @return The number of elements in the campaign list.
     */
    override fun getItemCount(): Int {
        return gamesList.size
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
        require(position >= 0 && position < gamesList.size) { "Invalid position in campaign list." }

        val campaignCardViewBinding = holder.viewBinding

        campaignCardViewBinding.gameCardName.text = gamesList[position].name
    }
}