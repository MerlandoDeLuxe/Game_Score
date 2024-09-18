package com.example.gamescore.presentation

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gamescore.domain.entity.Level

class GameViewModelFactory(private val level: Level, val application: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(level, application) as T
        }
        throw RuntimeException("Неизвестный класс $modelClass")
    }
}