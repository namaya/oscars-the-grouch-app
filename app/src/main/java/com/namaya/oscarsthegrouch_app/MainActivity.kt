package com.namaya.oscarsthegrouch_app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.namaya.oscarsthegrouch_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

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

        val layoutManager = LinearLayoutManager(this)
        binding.gamesListScreen.gamesListView.layoutManager = layoutManager
        binding.gamesListScreen.gamesListView.adapter = GamesListViewAdapter()

        binding.gamesListScreen.addGameButton.setOnClickListener { view ->
            val addGameMenu = PopupMenu(this, view)

            // Inflate the menu resource
            addGameMenu.menuInflater.inflate(R.menu.add_game_menu, addGameMenu.menu)

            // Set an item click listener
            addGameMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.addGameCreate -> {
                        // Handle "Create" action
                        Toast.makeText(this, "Create", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.addGameJoin -> {
                        // Handle "Join" action
                        Toast.makeText(this, "Join", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }

            // Show the PopupMenu
            addGameMenu.show()
        }

    }
}