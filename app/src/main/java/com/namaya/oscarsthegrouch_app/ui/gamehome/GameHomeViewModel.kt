package com.namaya.oscarsthegrouch_app.ui.gamehome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameHomeViewModel: ViewModel() {
    private val _players: MutableLiveData<List<Player>> = MutableLiveData(listOf())
    val players: LiveData<List<Player>> = _players

}