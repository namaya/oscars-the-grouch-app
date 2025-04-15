package com.namaya.oscarsthegrouch_app.ui.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.namaya.oscarsthegrouch_app.AuthViewModel
import com.namaya.oscarsthegrouch_app.databinding.LoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity: AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.fetchAvatars()

        viewModel.avatars.observe(this) { avatars ->
            val adapter = AvatarListViewAdapter {}

            binding.avatarList.layoutManager = LinearLayoutManager(this)
            binding.avatarList.adapter = adapter

            adapter.submitList(avatars)
        }
    }
}