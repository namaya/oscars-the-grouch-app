package com.namaya.oscarsthegrouch_app.ui.gamehome

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.namaya.oscarsthegrouch_app.databinding.AddPlayerScreenBinding
import com.namaya.oscarsthegrouch_app.ui.login.AvatarListViewAdapter
import com.namaya.oscarsthegrouch_app.ui.viewmodels.GamesViewModel
import com.namaya.oscarsthegrouch_app.ui.viewmodels.UserViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.namaya.oscarsthegrouch_app.ui.UiState

class AddPlayerFragment: Fragment() {
    private var _binding: AddPlayerScreenBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()
    private val gamesViewModel: GamesViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = AddPlayerScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userViewModel.fetchAvatars()

        val adapter = AvatarListViewAdapter {
            userViewModel.setAvatar(it)
        }

        userViewModel.avatars.observe(viewLifecycleOwner) {
            Log.d("AddPlayerFragment", "Avatars changed: $it")
            when (it) {
                is UiState.Success -> {
                    binding.avatarList.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
                    binding.avatarList.adapter = adapter

                    adapter.submitList(it.value)
                }
                is UiState.Error -> {
                    Log.e("AddPlayerFragment", it.message)
                    Toast.makeText(requireContext(), "Error loading avatars", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Log.d("AddPlayerFragment", "Loading avatars")
                }
            }
        }

        binding.loginButton.setOnClickListener {
            val name = binding.usernameInput.text.toString()
            if (name.isBlank()) {
                binding.usernameInput.error = "Name cannot be blank"
                return@setOnClickListener
            }

            val avatarUri = userViewModel.selectedAvatarUri.value
            if (avatarUri == null) {
                Toast.makeText(requireContext(), "Please select an avatar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            gamesViewModel.addPlayer(name, avatarUri)

            Toast.makeText(requireContext(), "Player added", Toast.LENGTH_SHORT).show()
            val navController = findNavController()

            navController.popBackStack()
        }
    }
}