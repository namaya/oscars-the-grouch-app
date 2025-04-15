package com.namaya.oscarsthegrouch_app.api

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.namaya.oscarsthegrouch_app.dataStore
import com.namaya.oscarsthegrouch_app.model.User
import kotlinx.coroutines.flow.map


class UserManager {

    private var currentUser: User? = null

    fun getCurrentUser(context: Context): User? {
        if (currentUser == null) {
            val userIdKey = stringPreferencesKey("userId")
            context.dataStore.data.map { preferences ->
                val userId = preferences[userIdKey]
                if (userId != null) {
                    currentUser = User(userId, "TODO: fetch name")
                }
            }
        }

        return currentUser
    }


    fun setCurrentUser(user: User) {
        currentUser = user
    }

    companion object {
        private var instance: UserManager? = null

        fun getInstance(): UserManager {
            if (instance == null) {
                instance = UserManager()
            }
            return instance!!
        }
    }
}