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
            Player(User(it.id, it.username, it.avatarUri), it.score, it.state)
        }

        return players
    }

    fun withPlayers(players: List<Player>): Game {
        return Game(id, name, state, ownerId, players, apiClient)
    }
}