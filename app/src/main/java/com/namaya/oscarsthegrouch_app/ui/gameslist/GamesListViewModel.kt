package com.namaya.oscarsthegrouch_app.ui.gameslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.namaya.oscarsthegrouch_app.model.Game
import androidx.lifecycle.viewModelScope
import com.namaya.oscarsthegrouch_app.ui.UiState
import kotlinx.coroutines.launch

class GamesListViewModel: ViewModel() {
    private val _gamesList = MutableLiveData<List<Game>>()
    val gamesList: LiveData<List<Game>> = _gamesList

    private val _uiState = MutableLiveData<UiState<List<Game>>>()
    val uiState: LiveData<UiState<List<Game>>> = _uiState

    init {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            _gamesList.value = mutableListOf(
                Game("1", "Game 1"),
                Game("2", "Game 2"),
                Game("3", "Game 3"),
            )
            _uiState.value = UiState.Success(_gamesList.value!!)
        }
    }
}