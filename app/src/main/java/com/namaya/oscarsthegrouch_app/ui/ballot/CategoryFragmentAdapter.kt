package com.namaya.oscarsthegrouch_app.ui.ballot

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.namaya.oscarsthegrouch_app.R
import com.namaya.oscarsthegrouch_app.databinding.BallotCategoryBinding
import com.namaya.oscarsthegrouch_app.model.Category

class CategoryFragmentAdapter(fragment: Fragment, private val categories: List<Category>): FragmentStateAdapter(fragment) {
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

//        val adapter = NomineeAdapter(category.nominees)
//        binding.optionsGroup.adapter = adapter
//        binding.optionsGroup.layoutManager = LinearLayoutManager(requireContext())

        binding.optionsGroup.setOnCheckedChangeListener { _, checkedId ->
            val viewModel: BallotViewModel by activityViewModels()

            when (checkedId) {
                R.id.option1 -> {
                    viewModel.answerCurrentCategory(0)
                    binding.option1.setBackgroundColor(Color.YELLOW)
                }
                R.id.option2 -> {
                    viewModel.answerCurrentCategory(0)
                    binding.option2.setBackgroundColor(Color.YELLOW)
                }
                R.id.option3 -> {
                    viewModel.answerCurrentCategory(0)
                    binding.option3.setBackgroundColor(Color.YELLOW)
                }
                R.id.option4 -> {
                    viewModel.answerCurrentCategory(0)
                    binding.option4.setBackgroundColor(Color.YELLOW)
                }
            }
        }
    }
}