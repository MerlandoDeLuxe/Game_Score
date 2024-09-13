package com.example.gamescore.domain.entity

data class GameSettings(
    val maxSumValue: Int,               //Максимально возможное значение суммы
    val minCountOfRightAnswers: Int,    //Минимальное количество ответов для победы
    val minPercentOfRightAnswer: Int,   //Минимальный процент правильных ответов
    val gameTimeInSeconds: Int          //Время игры в секундах
    )