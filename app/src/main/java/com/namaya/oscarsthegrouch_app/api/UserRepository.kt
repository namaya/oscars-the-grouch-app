package com.namaya.oscarsthegrouch_app.api

import com.namaya.oscarsthegrouch_app.model.User
import javax.inject.Inject

class UserRepository @Inject constructor(private val apiClient: BackendApiClient) {
    suspend fun createUser(name: String): User {
        val resBody = apiClient.createUser(BackendApiClient.CreateUserRequest(name))
        return User(resBody.id, name)
    }
}