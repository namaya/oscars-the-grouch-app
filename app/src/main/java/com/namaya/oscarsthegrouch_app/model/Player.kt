package com.namaya.oscarsthegrouch_app.model

class User(val name: String)
class Player(val user: User, val gameId: Int, val score: Int, val state: String = "Waiting")