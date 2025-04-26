package com.namaya.oscarsthegrouch_app.ui.login

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.namaya.oscarsthegrouch_app.databinding.UserAvatarBinding

class AvatarListViewAdapter(
    private val onItemClick: (String) -> Unit
): ListAdapter<String, AvatarListViewAdapter.ViewHolder>(AvatarDiffCallback) {

    private var selectedPos = RecyclerView.NO_POSITION

    inner class ViewHolder(private val viewBinding: UserAvatarBinding): RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(item: String) {
            val imageView = viewBinding.avatarImageView

            Glide.with(viewBinding.root.context)
                .load("http://10.0.2.2:8080$item")
                .override(200, 200)
//                .placeholder(R.drawable.avatar_placeholder)
//                .error(R.drawable.avatar_error)
                .circleCrop() // optional: for rounded avatars
                .into(imageView)

            if (selectedPos != adapterPosition) {
                viewBinding.root.setBackgroundColor(Color.TRANSPARENT)
            }

            viewBinding.root.setOnClickListener {
                val prevPos = selectedPos
                selectedPos = adapterPosition

                if (prevPos != RecyclerView.NO_POSITION) {
                    notifyItemChanged(prevPos)
                }

                viewBinding.root.setBackgroundColor(Color.BLUE)

                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding = UserAvatarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    object AvatarDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}