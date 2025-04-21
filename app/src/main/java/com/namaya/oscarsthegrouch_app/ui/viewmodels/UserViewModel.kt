package com.namaya.oscarsthegrouch_app.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.namaya.oscarsthegrouch_app.api.UserRepository
import com.namaya.oscarsthegrouch_app.model.User
import com.namaya.oscarsthegrouch_app.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableLiveData<UiState<User>>(UiState.Loading)
    val user: LiveData<UiState<User>> = _user

    fun fetchUser() {
        _user.value = UiState.Loading
        viewModelScope.launch {
            _user.value = try {
                val user = userRepository.getUser()
                if (user == null) { null } else { UiState.Success(user) }
            } catch (e: Exception) {
                e.printStackTrace()
                UiState.Error("Error fetching user")
            }
        }
    }

    fun createUser(name: String, avatarUri: String) {
        _user.value = UiState.Loading
        viewModelScope.launch {
            _user.value = try {
                val user = userRepository.createUser(name, avatarUri)
                UiState.Success(user)
            } catch (e: Exception) {
                e.printStackTrace()
                UiState.Error("Error creating user")
            }
        }
    }

    private val _avatars = MutableLiveData<UiState<List<String>>>(UiState.Loading)
    val avatars: LiveData<UiState<List<String>>> = _avatars

    val selectedAvatarUri: MutableLiveData<String> = MutableLiveData<String>()

    fun fetchAvatars() {
        _avatars.value = UiState.Loading
        viewModelScope.launch {
            _avatars.value = try {
                val avatars = userRepository.listAvatars()
                UiState.Success(avatars)
            } catch (e: Exception) {
                e.printStackTrace()
                UiState.Error("Error loading avatars")
            }
        }
    }

    fun setAvatar(avatarUri: String) {
        selectedAvatarUri.value = avatarUri
    }
}