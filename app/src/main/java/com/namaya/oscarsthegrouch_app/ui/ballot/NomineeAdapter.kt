package com.namaya.oscarsthegrouch_app.ui.ballot

import android.graphics.Color
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.namaya.oscarsthegrouch_app.R
import com.namaya.oscarsthegrouch_app.databinding.NomineeCardBinding
import com.namaya.oscarsthegrouch_app.databinding.UserAvatarBinding
import com.namaya.oscarsthegrouch_app.model.Nominee

class NomineeAdapter(
    private val onItemClick: (Nominee) -> Unit
) : ListAdapter<Nominee, NomineeAdapter.NomineeViewHolder>(ItemDiffCallback)
{
    private var selectedPos = RecyclerView.NO_POSITION

    inner class NomineeViewHolder(private val vb: NomineeCardBinding): RecyclerView.ViewHolder(vb.root) {
        fun bind(item: Nominee) {
            vb.nomineeTV.text = item.work + " - " + item.contributor

            if (selectedPos != adapterPosition) {
                vb.root.setBackgroundColor(Color.TRANSPARENT)
            }

            vb.root.setOnClickListener {
                val prevPos = selectedPos
                selectedPos = adapterPosition

                if (prevPos != RecyclerView.NO_POSITION) {
                    notifyItemChanged(prevPos)
                }

                vb.root.setBackgroundColor(Color.YELLOW)
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NomineeViewHolder {
        val vb = NomineeCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NomineeViewHolder(vb)
    }

    override fun onBindViewHolder(holder: NomineeViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    object ItemDiffCallback : DiffUtil.ItemCallback<Nominee>() {
        override fun areItemsTheSame(oldItem: Nominee, newItem: Nominee): Boolean {
            return oldItem.work == newItem.work && oldItem.contributor == newItem.contributor
        }

        override fun areContentsTheSame(oldItem: Nominee, newItem: Nominee): Boolean {
            return oldItem.work == newItem.work && oldItem.contributor == newItem.contributor
        }
    }
}