package com.namaya.oscarsthegrouch_app.ui.ballot

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.namaya.oscarsthegrouch_app.R
import com.namaya.oscarsthegrouch_app.databinding.BallotCategoryBinding
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

                    val adapter = QuestionIndexAdapter(
                        onItemClick = { index ->
//                            binding.viewPager.setCurrentItem(index, true)
                        }
                    )
                    // TODO: isAnswered could be true
                    val items =
                        List(uiState.questions.size) { index -> CategoryIndexItem(index, false) }

                    binding.statusIndicator.adapter = adapter
                    binding.viewPager.adapter = CategoryPagerAdapter(this, uiState.questions)

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

data class CategoryIndexItem(val index: Int, val isAnswered: Boolean)

class QuestionIndexAdapter(
    private val onItemClick: (Int) -> Unit
) : ListAdapter<CategoryIndexItem, QuestionIndexAdapter.IndexViewHolder>(QuestionIndexDiffCallback) {
    inner class IndexViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndexViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_index, parent, false) as TextView

        return IndexViewHolder(textView)
    }

    override fun onBindViewHolder(holder: IndexViewHolder, position: Int) {
        holder.textView.text = (position + 1).toString()

        val isAnswered = getItem(position).isAnswered

        holder.textView.setBackgroundResource(
            if (isAnswered) R.drawable.bg_index_answered else R.drawable.bg_index_unanswered
        )

        holder.textView.setOnClickListener {
            onItemClick(position)
        }
    }

    object QuestionIndexDiffCallback : DiffUtil.ItemCallback<CategoryIndexItem>() {
        override fun areItemsTheSame(oldItem: CategoryIndexItem, newItem: CategoryIndexItem): Boolean {
            return oldItem.index == newItem.index
        }

        override fun areContentsTheSame(oldItem: CategoryIndexItem, newItem: CategoryIndexItem): Boolean {
            return oldItem.isAnswered == newItem.isAnswered
        }
    }
}