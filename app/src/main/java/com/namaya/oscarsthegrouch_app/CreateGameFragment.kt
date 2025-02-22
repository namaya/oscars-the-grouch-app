package com.namaya.oscarsthegrouch_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.namaya.oscarsthegrouch_app.databinding.CreateGameScreenBinding

class CreateGameFragment: Fragment() {
    private var _binding: CreateGameScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = CreateGameScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        val ctx = requireContext()
        binding.createGameButton.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.toGameHomeScreen)
        }
    }
}