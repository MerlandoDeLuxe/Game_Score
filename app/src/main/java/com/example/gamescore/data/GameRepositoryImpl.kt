package com.example.gamescore.data

import com.example.gamescore.domain.entity.GameSettings
import com.example.gamescore.domain.entity.Level
import com.example.gamescore.domain.entity.Question
import com.example.gamescore.domain.repository.GameRepository
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object GameRepositoryImpl : GameRepository {

    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER_VALUE = 1


    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        val sum = Random.nextInt(MIN_SUM_VALUE, maxSumValue + 1)    //Значение суммы в кружке
        val visibleNumber = Random.nextInt(MIN_ANSWER_VALUE, sum)       //Видимое число в квадрате
        val options = HashSet<Int>() //коллекция для уникальных вариантов ответов
        val rightAnswer = sum - visibleNumber   //правильный ответ
        options.add(rightAnswer)    //кладем правильный ответ в уникальную коллекцию HashSet
        //нужно сгенерировать остальные вариантов ответов
        //согласно логике они находятся в небольшом диапазоне от правильного ответа

        //функция max вычисляет значение и, если оно оказывается отрицательным,
        //то принимает from принимает значение MIN_ANSWER_VALUE,
        //если оказывается положительным, то принимает значение rightAnswer - countOfOptions
        val from = max(rightAnswer - countOfOptions, MIN_ANSWER_VALUE)
        //нижняя граница диапазона. Либо максимальное значение всех элементов
        //либо rightAnswer - countOfOptions
        val to = min(maxSumValue, rightAnswer - countOfOptions)

        //генерируем значения пока не получим необходимое количество неправильных вариантов ответов
        while (options.size < countOfOptions) {
            options.add(Random.nextInt(from, to))
        }

        return Question(sum, visibleNumber, options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when (level){
            Level.TEST -> GameSettings(10,3,50,8)
            Level.EASY -> GameSettings(10,10,70,60)
            Level.MEDIUM -> GameSettings(20,20,80,40)
            Level.HARD -> GameSettings(30,30,90,40)
        }
    }
}