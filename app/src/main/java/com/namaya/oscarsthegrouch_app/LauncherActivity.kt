package com.namaya.oscarsthegrouch_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.namaya.oscarsthegrouch_app.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LauncherActivity: AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoggedIn.collect { loggedIn ->
                    when (loggedIn) {
                        true -> {
                            startActivity(Intent(this@LauncherActivity, MainActivity::class.java))
                            finish()
                        }
                        false -> {
                            startActivity(Intent(this@LauncherActivity, LoginActivity::class.java))
                            finish()
                        }
                        null -> {
                            // TODO: show loading screen
                        }
                    }
                }
            }

        }
    }
}