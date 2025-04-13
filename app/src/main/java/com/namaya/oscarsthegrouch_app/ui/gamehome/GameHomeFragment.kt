package com.namaya.oscarsthegrouch_app.ui.gamehome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.namaya.oscarsthegrouch_app.databinding.GameScreenBinding

class GameHomeFragment: Fragment() {
    private var _binding: GameScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = GameScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ctx = requireContext()
        val layoutManager = LinearLayoutManager(ctx)
        binding.playerListView.layoutManager = layoutManager
        binding.playerListView.adapter = PlayerListViewAdapter { it
            val navController = findNavController()
            val action = GameHomeFragmentDirections.toBallotScreen(it.user.name)
            navController.navigate(action)
        }

//        binding.addGameButton.setOnClickListener { gameButtonView ->
//            val addGameMenu = PopupMenu(ctx, gameButtonView)
//
//            // Inflate the menu resource
//            addGameMenu.menuInflater.inflate(R.menu.add_game_menu, addGameMenu.menu)
//
//            // Set an item click listener
//            addGameMenu.setOnMenuItemClickListener { item ->
//                when (item.itemId) {
//                    R.id.addGameCreate -> {
//                        // Handle "Create" action
//                        val navController = findNavController()
//                        navController.navigate(R.id.toGameCreateScreen)
//                        true
//                    }
//                    R.id.addGameJoin -> {
//                        // Handle "Join" action
//                        Toast.makeText(ctx, "Join", Toast.LENGTH_SHORT).show()
//                        true
//                    }
//                    else -> false
//                }
//            }
//
//            // Show the PopupMenu
//            addGameMenu.show()
//        }
    }
}