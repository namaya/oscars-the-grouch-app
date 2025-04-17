package com.namaya.oscarsthegrouch_app.model

class User(val id: String, val name: String, val avatarUri: String)
class Player(val user: User, val score: Int, val state: String = "Waiting")