package com.namaya.oscarsthegrouch_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.namaya.oscarsthegrouch_app.databinding.GameScreenBinding

class GameHomeFragment: Fragment() {
    private var _binding: GameScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = GameScreenBinding.inflate(inflater, container, false)
        return binding.root
    }
}