package com.namaya.oscarsthegrouch_app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.namaya.oscarsthegrouch_app.ui.viewmodels.UserViewModel
import com.namaya.oscarsthegrouch_app.databinding.LoginBinding
import com.namaya.oscarsthegrouch_app.ui.UiState

class LoginFragment: Fragment() {
    private var _binding: LoginBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = LoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userViewModel.fetchAvatars()

        val adapter = AvatarListViewAdapter {
            userViewModel.setAvatar(it)
        }

        userViewModel.avatars.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Success -> {
                    binding.avatarList.layoutManager = LinearLayoutManager(requireContext())
                    binding.avatarList.adapter = adapter

                    adapter.submitList(it.value)
                }
                else -> {}
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

            userViewModel.createUser(name, avatarUri)
        }
    }
}