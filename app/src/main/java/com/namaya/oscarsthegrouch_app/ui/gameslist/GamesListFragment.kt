package com.namaya.oscarsthegrouch_app.ui.gameslist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.namaya.oscarsthegrouch_app.R
import com.namaya.oscarsthegrouch_app.databinding.GamesListScreenBinding
import com.namaya.oscarsthegrouch_app.ui.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GamesListFragment: Fragment() {
    private var _binding: GamesListScreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GamesListViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = GamesListScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ctx = requireContext()

        setupGameListView(ctx)

        binding.addGameButton.setOnClickListener { gameButtonView ->
            // TODO: reuse popup menu (don't create new object everytime)
            val addGameMenu = PopupMenu(ctx, gameButtonView)

            // Inflate the menu resource
            addGameMenu.menuInflater.inflate(R.menu.add_game_menu, addGameMenu.menu)

            // Set an item click listener
            addGameMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.addGameCreate -> {
                        // Handle "Create" action
                        val navController = findNavController()
                        val action = GamesListFragmentDirections.toGameCreateScreen()
                        navController.navigate(action)
                        true
                    }
                    R.id.addGameJoin -> {
                        // Handle "Join" action
                        Toast.makeText(ctx, "Join", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }

            // Show the PopupMenu
            addGameMenu.show()
        }
    }

    private fun setupGameListView(ctx : Context) {
        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    binding.loadingView.visibility = View.VISIBLE
                    binding.gamesListView.visibility = View.GONE
                }
                is UiState.Success -> {
                    binding.loadingView.visibility = View.GONE
                    binding.gamesListView.visibility = View.VISIBLE

                    val layoutManager = LinearLayoutManager(ctx)

                    val adapter = GamesListViewAdapter {
                        val navController = findNavController()
                        val action = GamesListFragmentDirections.toGameHomeScreen(it.id)
                        navController.navigate(action)
                    }

                    binding.gamesListView.layoutManager = layoutManager
                    binding.gamesListView.adapter = adapter

                    viewModel.gamesList.observe(viewLifecycleOwner) { gamesList ->
                        adapter.submitList(gamesList)
                    }
                }
                is UiState.Error -> {
                    Toast.makeText(ctx, uiState.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}