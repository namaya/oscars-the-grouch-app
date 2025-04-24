package com.namaya.oscarsthegrouch_app.ui.answer

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

data class NomineeItem(val nominee: Nominee, val isSelected: Boolean)

class NomineeAdapter(
    private val onItemClick: (NomineeItem) -> Unit
) : ListAdapter<NomineeItem, NomineeAdapter.NomineeViewHolder>(ItemDiffCallback) {

    private var selectedPos = RecyclerView.NO_POSITION

    inner class NomineeViewHolder(private val vb: NomineeCardBinding) :
        RecyclerView.ViewHolder(vb.root) {
        fun bind(item: NomineeItem) {
            vb.nomineeTV.text = item.nominee.work + " - " + item.nominee.contributor

            vb.root.setBackgroundColor(if (item.isSelected) Color.YELLOW else Color.TRANSPARENT)

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

    object ItemDiffCallback : DiffUtil.ItemCallback<NomineeItem>() {
        override fun areItemsTheSame(oldItem: NomineeItem, newItem: NomineeItem): Boolean {
            return oldItem.nominee.work == newItem.nominee.work &&
                    oldItem.nominee.contributor == newItem.nominee.contributor
        }

        override fun areContentsTheSame(oldItem: NomineeItem, newItem: NomineeItem): Boolean {
            return oldItem == newItem
        }
    }
}