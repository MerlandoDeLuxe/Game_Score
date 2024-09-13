package com.example.gamescore.domain.repository

import com.example.gamescore.domain.entity.GameSettings
import com.example.gamescore.domain.entity.Level
import com.example.gamescore.domain.entity.Question

interface GameRepository {

    fun generateQuestion(
        maxSumValue: Int,   //Максимальное значение, которое нужно сгенерировать в поле сумма
        countOfOptions: Int //Сколько ответов нужно генерировать
    ): Question

    fun getGameSettings(level: Level): GameSettings
}