package com.namaya.oscarsthegrouch_app.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class Category(val name: String, val nominees: List<Nominee>, val guess: Int)
class Nominee(val subject: String, val background: String)

sealed class UiState {
    object Loading : UiState()
    data class Success(val questions: List<Category>) : UiState()
    data class Error(val message: String) : UiState()
}

class BallotViewModel: ViewModel() {
//    private val _categoryBank: MutableLiveData<List<Category>> = MutableLiveData(listOf())
//    val categoryBank: LiveData<List<Category>> = _categoryBank

    private val _currentCategoryIdx: MutableLiveData<Int> = MutableLiveData(0)
    val currentCategoryIdx: LiveData<Int> = _currentCategoryIdx

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    init {
        // TODO: load categories from server
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val categoryBank = listOf(
                    Category("Category A", listOf(Nominee("Subject 1", "Background 1"), Nominee("Subject 2", "Background 2")), 0),
                    Category("Category B", listOf(Nominee("Subject 1", "Background 1"), Nominee("Subject 2", "Background 2")), 0),
                    Category("Category C", listOf(Nominee("Subject 1", "Background 1"), Nominee("Subject 2", "Background 2")), 0),
                    Category("Category D", listOf(Nominee("Subject 1", "Background 1"), Nominee("Subject 2", "Background 2")), 0),
                )
                _uiState.value = UiState.Success(categoryBank)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error loading categories")
            }
        }
    }

    fun moveToCategory(index: Int) {
        _currentCategoryIdx.value = index
    }
}