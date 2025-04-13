package com.namaya.oscarsthegrouch_app.ui.ballot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.namaya.oscarsthegrouch_app.databinding.FragmentFillBallotBinding

class FillBallotFragment: Fragment() {
    private var _binding: FragmentFillBallotBinding? = null
    private val binding get() = _binding!! // only valid between onCreateView and onDestroyView

    private val viewModel: BallotViewModel by activityViewModels()
    private val naviArgs: FillBallotFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFillBallotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ballotsPlayerName.text = naviArgs.playerName

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    binding.loadingView.visibility = View.VISIBLE
                    binding.viewPager.visibility = View.GONE
                }
                is UiState.Success -> {
                    binding.loadingView.visibility = View.GONE
                    binding.viewPager.visibility = View.VISIBLE

                    binding.statusIndicator.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                    val adapter = CategoryIndexAdapter(
                        onItemClick = { index ->
//                            binding.viewPager.setCurrentItem(index, true)
                        }
                    )
                    // TODO: isAnswered could be true
                    val items =
                        List(uiState.questions.size) { index -> CategoryIndexItem(index, false) }

                    binding.statusIndicator.adapter = adapter
                    binding.viewPager.adapter = CategoryFragmentAdapter(this, uiState.questions)

                    adapter.submitList(items)

                    viewModel.categoryGuesses.observe(viewLifecycleOwner) { categoryGuesses ->
                        val newItems = List(adapter.currentList.size) { index ->
                            val isAnswered = categoryGuesses[uiState.questions[index].id] != null
                            CategoryIndexItem(index, isAnswered)
                        }
                        adapter.submitList(newItems)
                    }

                    binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            viewModel.moveToCategory(position)
                        }
                    })
                }
                is UiState.Error -> {
                    Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

