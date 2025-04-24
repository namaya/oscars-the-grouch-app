package com.namaya.oscarsthegrouch_app.ui.answer

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.namaya.oscarsthegrouch_app.R
import com.namaya.oscarsthegrouch_app.databinding.BallotCategoryBinding
import com.namaya.oscarsthegrouch_app.model.Category
import com.namaya.oscarsthegrouch_app.ui.UiState
import com.namaya.oscarsthegrouch_app.ui.viewmodels.BallotViewModel
import com.namaya.oscarsthegrouch_app.ui.viewmodels.GamesViewModel
import com.namaya.oscarsthegrouch_app.ui.viewmodels.UserViewModel
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
    val userViewModel: UserViewModel by activityViewModels()
    val gameViewModel: GamesViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BallotCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.questionText.text = category.name

        val adapter = NomineeAdapter {
            if (it.isSelected) return@NomineeAdapter

            val userId = when (val state = userViewModel.user.value) {
                is UiState.Success -> state.value.id
                else -> throw Exception("Invalid user state")
            }

            val gameId = when (val state = gameViewModel.selectedGame.value) {
                is UiState.Success -> state.value.id
                else -> throw Exception("Invalid game state")
            }

            val vote = category.nominees.indexOf(it.nominee)

            ballotViewModel.submitAnswer(userId, gameId, category.id, vote)

            val navController = findNavController()
            navController.popBackStack()
        }

        binding.choicesRV.adapter = adapter
        binding.choicesRV.layoutManager = LinearLayoutManager(requireContext())

        val vote = when (val state = ballotViewModel.masterBallotState.value) {
            is UiState.Success -> state.value.firstOrNull { it.categoryId == category.id }
            else -> throw Exception("Invalid ballot state")
        }

        if (vote == null) {
            adapter.submitList(category.nominees.map { NomineeItem(it, false) })
            return
        }

        val nomineeItems = category.nominees.mapIndexed { idx, nominee -> NomineeItem(nominee, vote.vote == idx) }

        adapter.submitList(nomineeItems)
    }
}