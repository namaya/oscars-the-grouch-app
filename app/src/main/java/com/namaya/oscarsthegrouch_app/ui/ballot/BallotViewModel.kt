package com.namaya.oscarsthegrouch_app.ui.ballot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namaya.oscarsthegrouch_app.model.Category
import com.namaya.oscarsthegrouch_app.model.Nominee
import kotlinx.coroutines.launch
import com.namaya.oscarsthegrouch_app.ui.UiState

class BallotViewModel: ViewModel() {
    private val _categoryBank: MutableLiveData<List<Category>> = MutableLiveData(listOf())
    val categoryBank: LiveData<List<Category>> = _categoryBank

    private val _uiState = MutableLiveData<UiState<List<Category>>>()
    val uiState: LiveData<UiState<List<Category>>> = _uiState

    private val _currentCategoryIdx: MutableLiveData<Int> = MutableLiveData(0)
    val currentCategoryIdx: LiveData<Int> = _currentCategoryIdx

    private val _categoryGuesses: MutableLiveData<Map<String, Int>> = MutableLiveData(mapOf())
    val categoryGuesses: LiveData<Map<String, Int>> = _categoryGuesses

    init {
        // TODO: load categories from server
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val categoryBank = listOf(
                    Category("1", "Category A", listOf(Nominee("Subject 1", "Background 1"), Nominee("Subject 2", "Background 2"))),
                    Category("2", "Category B", listOf(Nominee("Subject 1", "Background 1"), Nominee("Subject 2", "Background 2"))),
                    Category("3","Category C", listOf(Nominee("Subject 1", "Background 1"), Nominee("Subject 2", "Background 2"))),
                    Category("4","Category D", listOf(Nominee("Subject 1", "Background 1"), Nominee("Subject 2", "Background 2"))),
                )
                _categoryBank.value = categoryBank
                _uiState.value = UiState.Success(categoryBank)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error loading categories")
            }
        }
    }

    fun nextCategory() {
        val curIdx = _currentCategoryIdx.value!!
        _currentCategoryIdx.value = (curIdx + 1) % _categoryBank.value!!.size
    }

    fun prevCategory() {
        val curIdx = _currentCategoryIdx.value!!
        _currentCategoryIdx.value = (curIdx - 1 + _categoryBank.value!!.size) % _categoryBank.value!!.size
    }

    fun moveToCategory(index: Int) {
        _currentCategoryIdx.value = index
    }

    fun answerCurrentCategory(guess: Int) {
        val currentCategory = _categoryBank.value!![_currentCategoryIdx.value!!]
        require(guess >= 0 && guess < currentCategory.nominees.size)

        val newCategoryGuesses = _categoryGuesses.value!!.toMutableMap()
        newCategoryGuesses[currentCategory.id] = guess
        _categoryGuesses.value = newCategoryGuesses
    }
}