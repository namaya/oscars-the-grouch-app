package com.namaya.oscarsthegrouch_app.model

class Category(val id: String, val name: String, val nominees: List<Nominee>)
class Nominee(val subject: String, val background: String)