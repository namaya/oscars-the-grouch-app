package com.namaya.oscarsthegrouch_app.model

import com.namaya.oscarsthegrouch_app.api.BackendApiClient

data class Game(
    val id: String,
    val name: String,
    val state: String = "Created",
    val ownerId: String,
    val players: List<Player>,
    private val apiClient: BackendApiClient
) {
    suspend fun getPlayers(): List<Player> {
        val resBody = apiClient.getPlayers(ownerId, id)

        val players = resBody.players.map {
            Player(it.id, User(it.userId, it.username, it.avatarUri), it.score, it.state)
        }

        return players
    }

    suspend fun addPlayer(name: String, avatarUri: String): Player {
        val resBody = apiClient.addPlayer(ownerId, id, BackendApiClient.AddPlayerRequest(name, avatarUri))
        val player = Player(resBody.id, User(resBody.userId, name, avatarUri), 0, "Waiting")
        return player
    }

    suspend fun withState(state: String): Game {
//        apiClient.startGame(ownerId, id)
        return Game(id, name, state, ownerId, players, apiClient)
    }

    suspend fun end() {
//        apiClient.endGame(ownerId, id)
    }

    fun withPlayers(players: List<Player>): Game {
        return Game(id, name, state, ownerId, players, apiClient)
    }
}