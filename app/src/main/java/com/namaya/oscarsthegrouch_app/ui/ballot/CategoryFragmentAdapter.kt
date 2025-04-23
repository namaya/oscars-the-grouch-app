package com.namaya.oscarsthegrouch_app.ui.ballot

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.namaya.oscarsthegrouch_app.R
import com.namaya.oscarsthegrouch_app.databinding.BallotCategoryBinding
import com.namaya.oscarsthegrouch_app.model.Category
import com.namaya.oscarsthegrouch_app.ui.viewmodels.BallotViewModel
import dagger.hilt.android.AndroidEntryPoint

class CategoryFragmentAdapter(fragment: Fragment, private val categories: List<Category>): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return categories.size
    }
    override fun createFragment(position: Int): Fragment {
        require(position >= 0 && position < (categories.size)) { "Invalid position in categories list." }
        return CategoryFragment(categories[position])
    }
}

@AndroidEntryPoint
class CategoryFragment(private val category: Category): Fragment() {
    private var _binding: BallotCategoryBinding? = null
    private val binding get() = _binding!! // only valid between onCreateView and onDestroyView

    val ballotViewModel: BallotViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BallotCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.questionText.text = category.name

        val adapter = NomineeAdapter {
            ballotViewModel.answerCategory(it)
        }

        binding.choicesRV.adapter = adapter
        binding.choicesRV.layoutManager = LinearLayoutManager(requireContext())

        val nomineeItems = category.nominees.map { NomineeItem(it, it == ballotViewModel.selectedPlayerState.value!!.categoryGuesses[category.id]) }

        adapter.submitList(nomineeItems)
    }
}