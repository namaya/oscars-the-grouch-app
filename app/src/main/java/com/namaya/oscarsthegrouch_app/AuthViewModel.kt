package com.namaya.oscarsthegrouch_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namaya.oscarsthegrouch_app.api.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn.asStateFlow()

    private val _avatars = MutableLiveData<List<String>>(emptyList())
    val avatars: LiveData<List<String>> = _avatars

    init {
        viewModelScope.launch {
            val loggedIn = userRepository.isLoggedIn()
            _isLoggedIn.value = loggedIn
        }
    }

    fun fetchAvatars() {
        viewModelScope.launch {
            _avatars.value = userRepository.listAvatars()
        }
    }
}