package com.namaya.oscarsthegrouch_app.api

import android.content.Context
import com.namaya.oscarsthegrouch_app.model.Game
import com.namaya.oscarsthegrouch_app.model.User
import retrofit2.HttpException
import javax.inject.Inject

class GameRepository @Inject constructor(private val apiClient: BackendApiClient) {
    suspend fun listGames(): List<Game> {
//        val user = UserManager.getInstance().getCurrentUser(ctx)
//        val token = user?.id ?: ""
//
//        try {
//            val resBody = apiClient.listGames(token)
//        } catch (he: HttpException) {
//            if (he.code() == 401) {
//                // TODO: handle unauthorized
//                apiClient.createUser(BackendApiClient.CreateUserRequest("name1"))
//            }
//        }
//
//        return resBody.games
        return emptyList()
    }
}