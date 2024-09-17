package com.example.gamescore.presentation

import androidx.lifecycle.ViewModel
import com.example.gamescore.data.GameRepositoryImpl
import com.example.gamescore.domain.entity.GameSettings
import com.example.gamescore.domain.entity.Level
import com.example.gamescore.domain.entity.Question
import com.example.gamescore.domain.usecases.GenerateQuestionUseCase
import com.example.gamescore.domain.usecases.GetGameSettingsUseCase

class GameViewModel(private val level: Level) : ViewModel() {
    private val repository = GameRepositoryImpl

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private var maxSumValue = 0

    init {
        getGameSettings()
    }

    private fun getGameSettings() {
        val gameSettings = getGameSettingsUseCase.invoke(level)
        maxSumValue = gameSettings.maxSumValue
    }

    fun generateQuestion(): Question {
        return generateQuestionUseCase.invoke(maxSumValue)
    }
}