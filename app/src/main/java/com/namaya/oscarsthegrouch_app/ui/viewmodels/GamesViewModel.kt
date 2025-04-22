package com.namaya.oscarsthegrouch_app.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.namaya.oscarsthegrouch_app.model.Game
import androidx.lifecycle.viewModelScope
import com.namaya.oscarsthegrouch_app.api.GameRepository
import com.namaya.oscarsthegrouch_app.model.Player
import com.namaya.oscarsthegrouch_app.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(
    private val gameRepo: GameRepository,
): ViewModel() {
    private val _gamesList = MutableLiveData<UiState<List<Game>>>()
    val gamesList: LiveData<UiState<List<Game>>> = _gamesList

    val selectedGame: MutableLiveData<UiState<Game>> = MutableLiveData<UiState<Game>>()

    private val _players: MutableLiveData<UiState<List<Player>>> = MutableLiveData(UiState.Loading)
    val players: LiveData<UiState<List<Player>>> = _players

    val selectedPlayer: MutableLiveData<Player> = MutableLiveData<Player>()

    // TODO: set up loading screen via ui state
    fun fetchGames(userId: String) {
        _gamesList.value = UiState.Loading
        viewModelScope.launch {
            _gamesList.value = try {
                val games = gameRepo.listGames(userId)
                UiState.Success(games)
            } catch (e: Exception) {
                UiState.Error("Error loading games")
            }
        }
    }

    fun createGame(userId: String, name: String) {
        viewModelScope.launch {
            val game = gameRepo.createGame(userId, name)
            selectedGame.value = UiState.Success(game)
        }
    }

    fun fetchPlayers(game: Game) {
        viewModelScope.launch {
            _players.value = try {
                val players = game.getPlayers()
                UiState.Success(players)
            } catch (e: Exception) {
                UiState.Error("Error loading players")
            }
        }
    }

    val addedPlayerState = MutableLiveData<UiState<Player>>()

    fun addPlayer(name: String, avatarUri: String) {
        viewModelScope.launch {
            val game = when (val game = selectedGame.value) {
                is UiState.Success -> game.value
                else -> return@launch
            }

            val player = game.addPlayer(name, avatarUri)

            _players.value =
                when (val players = _players.value) {
                    is UiState.Success -> UiState.Success(players.value + player)
                    else -> UiState.Error("Error adding player")
                }
            addedPlayerState.value = UiState.Success(player)
        }
    }
}