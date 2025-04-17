package com.namaya.oscarsthegrouch_app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.namaya.oscarsthegrouch_app.databinding.ActivityMainBinding
import com.namaya.oscarsthegrouch_app.ui.login.LoginFragmentDirections
import com.namaya.oscarsthegrouch_app.ui.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
                val navController = navHostFragment.navController

                viewModel.isLoggedIn.collect { loggedIn ->
                    when (loggedIn) {
                        true -> {
                            val action = LoginFragmentDirections.toGamesListScreen()
                            navController.navigate(action)
                        }

                        false -> {}
                        null -> {}
                    }
                }
            }
        }
    }
}