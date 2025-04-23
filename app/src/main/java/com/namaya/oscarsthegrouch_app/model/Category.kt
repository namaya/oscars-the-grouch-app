package com.namaya.oscarsthegrouch_app.model

data class Category(val id: String, val name: String, val nominees: List<Nominee>)
data class Nominee(val work: String, val contributor: String)

data class Vote(val categoryId: String, val vote: Int)
data class Ballot(val id: String, val votes: List<Vote>)