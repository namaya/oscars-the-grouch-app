package com.namaya.oscarsthegrouch_app.api

import android.content.Context
import com.namaya.oscarsthegrouch_app.model.Category
import com.namaya.oscarsthegrouch_app.model.Game
import com.namaya.oscarsthegrouch_app.model.User
import retrofit2.HttpException
import javax.inject.Inject

class GameRepository @Inject constructor(private val apiClient: BackendApiClient) {
    suspend fun listGames(userId: String): List<Game> {
        val resBody = apiClient.listGames(userId)

        val games = resBody.games.map {
            Game(it.id, it.name, it.state, userId, emptyList(), apiClient)
        }

        return games
    }

    suspend fun createGame(userId: String, name: String): Game {
        val resBody = apiClient.createGame(userId, BackendApiClient.CreateGameRequest(name))
        return Game(resBody.id, resBody.name, resBody.state, userId, emptyList(), apiClient)
    }

    suspend fun listCategories(userId: String, gameId: String): List<Category> {
        val resBody = apiClient.listNominations(userId, gameId)

        val categories = resBody.categories.map {
            Category(it.id, it.name, it.nominees)
        }

        return categories
    }

}