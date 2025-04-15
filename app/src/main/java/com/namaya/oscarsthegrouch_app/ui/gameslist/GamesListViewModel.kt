package com.namaya.oscarsthegrouch_app.ui.gameslist

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.namaya.oscarsthegrouch_app.model.Game
import androidx.lifecycle.viewModelScope
import com.namaya.oscarsthegrouch_app.api.BackendApiClient
import com.namaya.oscarsthegrouch_app.api.GameRepository
import com.namaya.oscarsthegrouch_app.api.UserRepository
import com.namaya.oscarsthegrouch_app.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class GamesListViewModel @Inject constructor(
    private val gameRepo: GameRepository,
): ViewModel() {
    private val _gamesList = MutableLiveData<List<Game>>()
    val gamesList: LiveData<List<Game>> = _gamesList

    private val _uiState = MutableLiveData<UiState<List<Game>>>()
    val uiState: LiveData<UiState<List<Game>>> = _uiState

    // TODO: load games from server
    // TODO: share view model with other fragments
    // TODO: set up loading screen via ui state
    init {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                _gamesList.value = gameRepo.listGames()
                _uiState.value = UiState.Success(_gamesList.value!!)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error loading games")
            }

        }
    }
}