package com.namaya.oscarsthegrouch_app.ui.ballot

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.namaya.oscarsthegrouch_app.databinding.FragmentFillBallotBinding
import com.namaya.oscarsthegrouch_app.ui.UiState
import com.namaya.oscarsthegrouch_app.ui.viewmodels.BallotViewModel
import com.namaya.oscarsthegrouch_app.ui.viewmodels.GamesViewModel
import com.namaya.oscarsthegrouch_app.ui.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FillBallotFragment: Fragment() {
    private var _binding: FragmentFillBallotBinding? = null
    private val binding get() = _binding!! // only valid between onCreateView and onDestroyView

    private val userViewModel: UserViewModel by activityViewModels()
    private val gamesViewModel: GamesViewModel by activityViewModels()
    private val ballotViewModel: BallotViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFillBallotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CategoryIndexAdapter {
            binding.viewPager.currentItem = it
        }

        gamesViewModel.selectedPlayer.observe(viewLifecycleOwner) {
            ballotViewModel.initializePlayer(it.id)
            binding.ballotsPlayerName.text = it.user.name
        }

        ballotViewModel.selectedPlayerState.observe(viewLifecycleOwner) {
            val cb = when (val state = ballotViewModel.categoryBank.value) {
                is UiState.Success -> state.value
                is UiState.Loading -> emptyList()
                else -> throw Exception("Invalid state")
            }

            val items =
                List(cb.size) { index -> CategoryIndexItem(index, it.categoryGuesses[cb[index].id] != null) }

            Log.d("FillBallotFragment", "items: $items")

            adapter.submitList(items)
        }

        ballotViewModel.categoryBank.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    binding.loadingView.visibility = View.VISIBLE
                    binding.viewPager.visibility = View.GONE
                }
                is UiState.Success -> {
                    binding.loadingView.visibility = View.GONE
                    binding.viewPager.visibility = View.VISIBLE

                    binding.statusIndicator.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    binding.statusIndicator.adapter = adapter
                    binding.viewPager.adapter = CategoryFragmentAdapter(this, it.value)

                    binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            ballotViewModel.moveToCategory(position)
                        }
                    })
                }
                is UiState.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        ballotViewModel.selectedPlayerState.observe(viewLifecycleOwner) {
            val cb = when (val state = ballotViewModel.categoryBank.value) {
                is UiState.Success -> state.value
                is UiState.Loading -> emptyList()
                else -> throw Exception("Invalid state")
            }

            val newItems = List(adapter.currentList.size) { index ->
                val isAnswered = it.categoryGuesses[cb[index].id] != null
                CategoryIndexItem(index, isAnswered)
            }

            adapter.submitList(newItems)
        }

        binding.homeB.setOnClickListener {
            ballotViewModel.savePlayerState()
            val navController = findNavController()
            val action = FillBallotFragmentDirections.toGameHomeScreen()
            navController.navigate(action)
        }

        binding.submitB.setOnClickListener {
            val userId = when (val state = userViewModel.user.value) {
                is UiState.Success -> state.value.id
                else -> throw Exception("Invalid state")
            }

            val gameId = when (val state = gamesViewModel.selectedGame.value) {
                is UiState.Success -> state.value.id
                else -> throw Exception("Invalid state")
            }

            ballotViewModel.submitBallot(userId, gameId)
            val navController = findNavController()
            val action = FillBallotFragmentDirections.toGameHomeScreen()
            navController.navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

