package com.namaya.oscarsthegrouch_app.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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

    suspend fun createUser(name: String): User {
        val resBody = apiClient.createUser(BackendApiClient.CreateUserRequest(name))
        return User(resBody.id, name)
    }

    suspend fun listAvatars(): List<String> {
        val resp = apiClient.listAvatars()
        return resp.avatars
    }

    suspend fun getAvatar(avatar: String): Bitmap? {
        val response = apiClient.getAvatar(avatar)

        return if (response.isSuccessful) {
            val body = response.body()?.byteStream()
            BitmapFactory.decodeStream(body)
        } else {
            null
        }
    }

    suspend fun isLoggedIn(): Boolean {
        val prefs = dataStore.data.first()
        return prefs[USER_ID_KEY] != null
    }
}