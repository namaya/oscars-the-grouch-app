package com.namaya.oscarsthegrouch_app.ui.gamehome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.namaya.oscarsthegrouch_app.databinding.EndgameScreenBinding
import com.namaya.oscarsthegrouch_app.databinding.GameScreenBinding
import com.namaya.oscarsthegrouch_app.ui.UiState
import com.namaya.oscarsthegrouch_app.ui.viewmodels.BallotViewModel
import com.namaya.oscarsthegrouch_app.ui.viewmodels.GamesViewModel

class EndGameFragment: Fragment() {
    private var _binding: EndgameScreenBinding? = null
    private val binding get() = _binding!!

    private val gamesViewModel: GamesViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = EndgameScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val winner = when (val game = gamesViewModel.players.value) {
            is UiState.Success -> game.value.maxByOrNull { it.score }
            else -> null
        }
        binding.congratsTV.text = "Congratulations, ${winner?.user?.name}!"
    }
}