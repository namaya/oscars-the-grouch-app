package com.namaya.oscarsthegrouch_app.ui.ballot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.namaya.oscarsthegrouch_app.databinding.FragmentFillBallotBinding
import com.namaya.oscarsthegrouch_app.ui.UiState
import com.namaya.oscarsthegrouch_app.ui.viewmodels.BallotViewModel
import com.namaya.oscarsthegrouch_app.ui.viewmodels.GamesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FillBallotFragment: Fragment() {
    private var _binding: FragmentFillBallotBinding? = null
    private val binding get() = _binding!! // only valid between onCreateView and onDestroyView

    private val gamesViewModel: GamesViewModel by activityViewModels()
    private val ballotViewModel: BallotViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFillBallotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gamesViewModel.selectedPlayer.observe(viewLifecycleOwner) {
            binding.ballotsPlayerName.text = it.user.name
        }

            val adapter = CategoryIndexAdapter(
            onItemClick = { index ->
//                            binding.viewPager.setCurrentItem(index, true)
            }
        )

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

                    // TODO: isAnswered could be true
                    val items =
                        List(it.value.size) { index -> CategoryIndexItem(index, false) }

                    binding.statusIndicator.adapter = adapter
                    binding.viewPager.adapter = CategoryFragmentAdapter(this, it.value)

                    adapter.submitList(items)

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

        ballotViewModel.categoryGuesses.observe(viewLifecycleOwner) {
//            val newItems = List(adapter.currentList.size) { index ->
//                val isAnswered = it[ca.value[index].id] != null
//                CategoryIndexItem(index, isAnswered)
//            }
//            adapter.submitList(newItems)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

