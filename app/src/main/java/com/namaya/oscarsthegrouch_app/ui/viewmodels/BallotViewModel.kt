package com.namaya.oscarsthegrouch_app.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namaya.oscarsthegrouch_app.api.GameRepository
import com.namaya.oscarsthegrouch_app.model.Category
import com.namaya.oscarsthegrouch_app.model.Game
import com.namaya.oscarsthegrouch_app.model.Nominee
import com.namaya.oscarsthegrouch_app.model.Player
import com.namaya.oscarsthegrouch_app.model.Vote
import kotlinx.coroutines.launch
import com.namaya.oscarsthegrouch_app.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.selects.select
import javax.inject.Inject

data class PlayerCategoryState(
    val playerId: String = "",
    val selectedCategoryIdx: Int = 0,
    val categoryGuesses: Map<String, Nominee> = mapOf()
)

@HiltViewModel
class BallotViewModel @Inject constructor(
    private val gameRepository: GameRepository
): ViewModel() {
    private val _categoryBank: MutableLiveData<UiState<List<Category>>> = MutableLiveData(UiState.Loading)
    val categoryBank: LiveData<UiState<List<Category>>> = _categoryBank

    fun fetchCategories(game: Game) {
        _categoryBank.value = UiState.Loading
        viewModelScope.launch {
            _categoryBank.value = try {
                val categories = gameRepository.listCategories(game.ownerId, game.id)
                UiState.Success(categories)
            } catch (e: Exception) {
                UiState.Error("Error loading categories")
            }
        }
    }

    private val _playerCategoryStates: MutableLiveData<Map<String, PlayerCategoryState>> = MutableLiveData(mapOf())
    val playerCategoryStates: LiveData<Map<String, PlayerCategoryState>> = _playerCategoryStates
    val selectedPlayerState: MutableLiveData<PlayerCategoryState> = MutableLiveData(null)

    fun initializePlayer(playerId: String) {
        if (!_playerCategoryStates.value!!.containsKey(playerId)) {
            _playerCategoryStates.value = _playerCategoryStates.value!!.toMutableMap().apply {
                Log.d("BallotViewModel", "Resetting player $playerId")
                this[playerId] = PlayerCategoryState(playerId=playerId)
            }
        }
        selectedPlayerState.value = _playerCategoryStates.value!![playerId]!!
    }

    fun moveToCategory(index: Int) {
        selectedPlayerState.value = selectedPlayerState.value!!.copy(selectedCategoryIdx = index)
    }

    fun answerCategory(guess: Nominee) {
        val currentCategory = when (val state = _categoryBank.value) {
            is UiState.Success -> {
                state.value[selectedPlayerState.value!!.selectedCategoryIdx]
            }
            else -> {
                Log.e("BallotViewModel", "Category bank not loaded properly")
                return
            }
        }

        val newCategoryGuesses = selectedPlayerState.value!!.categoryGuesses.toMutableMap()
        newCategoryGuesses[currentCategory.id] = guess

        selectedPlayerState.value = selectedPlayerState.value!!.copy(categoryGuesses = newCategoryGuesses)
    }

    fun savePlayerState() {
        val playerId = selectedPlayerState.value!!.playerId
        val playerState = selectedPlayerState.value!!

        _playerCategoryStates.value = _playerCategoryStates.value!!.toMutableMap().apply {
            Log.d("BallotViewModel", "Saving player $playerId")
            this[playerId] = playerState
        }
    }

    fun submitBallot(userId: String, gameId: String) {
        viewModelScope.launch {
            val cb = when (val state = _categoryBank.value) {
                is UiState.Success -> state.value
                is UiState.Loading -> emptyList()
                else -> throw Exception("Invalid state")
            }

            val playerId = selectedPlayerState.value!!.playerId
            val votes = selectedPlayerState.value!!.categoryGuesses.map {
                // TODO: verify this works
                val vote = cb.filter { cat -> cat.id == it.key }
                    .map { cat -> cat.nominees.indexOf(it.value) }
                    .first()

                Vote(it.key, vote)
            }

            gameRepository.submitBallot(userId, gameId, playerId, votes)
        }
    }

}