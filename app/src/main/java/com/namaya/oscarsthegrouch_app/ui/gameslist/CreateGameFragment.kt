package com.namaya.oscarsthegrouch_app.ui.gameslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.namaya.oscarsthegrouch_app.databinding.CreateGameScreenBinding
import com.namaya.oscarsthegrouch_app.ui.UiState
import com.namaya.oscarsthegrouch_app.ui.viewmodels.GamesViewModel
import com.namaya.oscarsthegrouch_app.ui.viewmodels.UserViewModel

class CreateGameFragment: Fragment() {
    private var _binding: CreateGameScreenBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()
    private val gamesViewModel: GamesViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = CreateGameScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        val ctx = requireContext()
        binding.createGameButton.setOnClickListener {
            if (binding.gameNameET.text.isEmpty()) {
                binding.gameNameET.error = "Please enter a game name"
                return@setOnClickListener
            }

            val gameName = binding.gameNameET.text.toString()
            val userId = when (val user = userViewModel.user.value) {
                is UiState.Success -> user.value.id
                else -> return@setOnClickListener
            }

            gamesViewModel.createGame(userId, gameName)
        }

        gamesViewModel.selectedGame.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Success -> {
                    val navController = findNavController()
                    val action = CreateGameFragmentDirections.toGameHomeScreen()
                    navController.navigate(action)
                }
                else -> {}
            }
        }
    }
}