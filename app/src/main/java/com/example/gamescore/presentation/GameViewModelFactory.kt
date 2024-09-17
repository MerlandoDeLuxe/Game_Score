package com.example.gamescore.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gamescore.domain.entity.Level

open class GameViewModelFactory (private val level: Level) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GameViewModel(level) as T
    }
}