package com.namaya.oscarsthegrouch_app.ui.answer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.namaya.oscarsthegrouch_app.databinding.AnswerScreenBinding
import com.namaya.oscarsthegrouch_app.ui.UiState
import com.namaya.oscarsthegrouch_app.ui.viewmodels.BallotViewModel
import com.namaya.oscarsthegrouch_app.ui.viewmodels.GamesViewModel
import com.namaya.oscarsthegrouch_app.ui.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnswerFragment: Fragment() {
    private var _binding: AnswerScreenBinding? = null
    private val binding get() = _binding!! // only valid between onCreateView and onDestroyView

    private val userViewModel: UserViewModel by activityViewModels()
    private val gamesViewModel: GamesViewModel by activityViewModels()
    private val ballotViewModel: BallotViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = AnswerScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CategoryIndexAdapter {
            binding.viewPager.currentItem = it
        }

        ballotViewModel.masterBallotState.observe(viewLifecycleOwner) {
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

                    val cb = when (val state = ballotViewModel.categoryBank.value) {
                        is UiState.Success -> state.value
                        is UiState.Loading -> emptyList()
                        else -> throw Exception("Invalid state")
                    }

                    val votes = it.value.map { vote -> vote.categoryId }

                    val items =
                        List(cb.size) { index -> CategoryIndexItem(index, cb[index].id in votes) }

                    adapter.submitList(items)

                    binding.viewPager.adapter = CategoryFragmentAdapter(this, cb)

                    binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            ballotViewModel.moveMasterBallotCategory(position)
                        }
                    })
                }
                is UiState.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

