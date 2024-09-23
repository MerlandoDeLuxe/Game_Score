package com.example.gamescore.data

import android.util.Log
import com.example.gamescore.domain.entity.GameSettings
import com.example.gamescore.domain.entity.Level
import com.example.gamescore.domain.entity.Question
import com.example.gamescore.domain.repository.GameRepository
import com.example.gamescore.domain.usecases.GenerateQuestionUseCase
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object GameRepositoryImpl : GameRepository {
    private const val TAG = "GameRepositoryImpl"

    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER_VALUE = 1


    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        val sum = makingRandomFigure(maxSumValue, MIN_SUM_VALUE)
        val mark: String

        var visibleNumber = 0
        var rightAnswer = 0

        do {
            visibleNumber = makingRandomFigure(maxSumValue, MIN_SUM_VALUE)
        } while (visibleNumber == sum)

        if (makingRandomBoolean()) {
            rightAnswer = sum + visibleNumber
            mark = "+"
        } else {
            rightAnswer = sum - visibleNumber
            mark = "-"
        }

        val options: HashSet<Int> = hashSetOf(rightAnswer)

        while (options.size < countOfOptions) {
            if (rightAnswer < 0) {
                options.add(makingRandomFigure(maxSumValue, rightAnswer))   //Это чтобы в генерацию попадали числа отрицательные тоже, чтобы было интереснее
            }
            options.add(makingRandomFigure(maxSumValue, MIN_SUM_VALUE))
        }

        return Question(sum, visibleNumber, rightAnswer, mark, options.toList())
    }

    private fun makingRandomFigure(high: Int, low: Int): Int {
        return Random.nextInt(high - low) + low
    }

    private fun makingRandomBoolean() = Random.nextBoolean()

    override fun getGameSettings(level: Level): GameSettings {
        return when (level) {
            Level.TEST -> GameSettings(10, 3, 50, 3)
            Level.EASY -> GameSettings(10, 10, 70, 20)
            Level.MEDIUM -> GameSettings(20, 20, 80, 30)
            Level.HARD -> GameSettings(30, 30, 90, 40)
        }
    }
}