package com.example.gamescore.domain.usecases

import com.example.gamescore.domain.entity.GameSettings
import com.example.gamescore.domain.entity.Level
import com.example.gamescore.domain.repository.GameRepository

class GetGameSettingsUseCase(private val gameRepository: GameRepository) {

    operator fun invoke(level: Level): GameSettings {
        return gameRepository.getGameSettings(level)
    }
}