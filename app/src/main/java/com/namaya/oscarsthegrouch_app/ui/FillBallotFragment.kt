package com.namaya.oscarsthegrouch_app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.namaya.oscarsthegrouch_app.databinding.BallotCategoryBinding
import com.namaya.oscarsthegrouch_app.databinding.FragmentFillBallotBinding

class FillBallotFragment: Fragment() {
    private var _binding: FragmentFillBallotBinding? = null
    private val binding get() = _binding!! // only valid between onCreateView and onDestroyView

    private val viewModel: BallotViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFillBallotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    binding.loadingView.visibility = View.VISIBLE
                    binding.viewPager.visibility = View.GONE
                }
                is UiState.Success -> {
                    binding.loadingView.visibility = View.GONE
                    binding.viewPager.visibility = View.VISIBLE

                    binding.viewPager.adapter = CategoryPagerAdapter(this, uiState.questions)
                }
                is UiState.Error -> {
                    Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

//        binding.statusIndicator.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class CategoryPagerAdapter(fragment: Fragment, private val categories: List<Category>): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return categories.size
    }
    override fun createFragment(position: Int): Fragment {
        require(position >= 0 && position < (categories.size)) { "Invalid position in categories list." }
        return CategoryFragment(categories[position])
    }
}

class CategoryFragment(private val category: Category): Fragment() {
    private var _binding: BallotCategoryBinding? = null
    private val binding get() = _binding!! // only valid between onCreateView and onDestroyView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BallotCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.questionText.text = category.name
    }
}