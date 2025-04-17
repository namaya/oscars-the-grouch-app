package com.namaya.oscarsthegrouch_app.api

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.namaya.oscarsthegrouch_app.model.User
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiClient: BackendApiClient,
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val USER_ID_KEY = stringPreferencesKey("user_id")
    }

    suspend fun createUser(name: String, avatarUri: String): User {
        val resBody = apiClient.createUser(BackendApiClient.CreateUserRequest(name, avatarUri))
        return User(resBody.userId, name, avatarUri)
    }

    suspend fun listAvatars(): List<String> {
        val resp = apiClient.listAvatars()
        return resp.avatars
    }

    suspend fun getUser(): User? {
        val prefs = dataStore.data.first()
        val userId = prefs[USER_ID_KEY] ?: return null
        val resBody = apiClient.getUser(userId)
        return User(resBody.userId, resBody.name, resBody.avatarUri)
    }
}