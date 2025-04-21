package com.namaya.oscarsthegrouch_app.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namaya.oscarsthegrouch_app.api.GameRepository
import com.namaya.oscarsthegrouch_app.model.Category
import com.namaya.oscarsthegrouch_app.model.Game
import kotlinx.coroutines.launch
import com.namaya.oscarsthegrouch_app.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

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

    private val _currentCategoryIdx: MutableLiveData<Int> = MutableLiveData(0)

    private val _categoryGuesses: MutableLiveData<Map<String, Int>> = MutableLiveData(mapOf())
    val categoryGuesses: LiveData<Map<String, Int>> = _categoryGuesses

    fun moveToCategory(index: Int) {
        _currentCategoryIdx.value = index
    }

    fun answerCurrentCategory(guess: Int) {
        val currentCategory = when (val state = _categoryBank.value) {
            is UiState.Success -> {
                state.value[_currentCategoryIdx.value!!]
            }
            else -> throw Exception("Invalid state")
        }

        require(guess >= 0 && guess < currentCategory.nominees.size)

        val newCategoryGuesses = _categoryGuesses.value!!.toMutableMap()
        newCategoryGuesses[currentCategory.id] = guess
        _categoryGuesses.value = newCategoryGuesses
    }
}