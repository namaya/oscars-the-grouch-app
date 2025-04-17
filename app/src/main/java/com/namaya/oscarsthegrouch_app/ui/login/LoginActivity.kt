package com.namaya.oscarsthegrouch_app.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.namaya.oscarsthegrouch_app.ui.viewmodels.AuthViewModel
import com.namaya.oscarsthegrouch_app.databinding.LoginBinding

class LoginFragment: Fragment() {
    private var _binding: LoginBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = LoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userViewModel.fetchAvatars()

        userViewModel.avatars.observe(viewLifecycleOwner) { avatars ->
            val adapter = AvatarListViewAdapter {}

            binding.avatarList.layoutManager = LinearLayoutManager(requireContext())
            binding.avatarList.adapter = adapter

            adapter.submitList(avatars)
        }
    }
}