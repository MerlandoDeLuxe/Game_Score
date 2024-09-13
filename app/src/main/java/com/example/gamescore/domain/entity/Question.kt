package com.example.gamescore.domain.entity

data class Question(
    val sum: Int,                   //Отображает значение суммы в кружке
    val visibleNumber: Int,         //Отображает видимое число в левом квадрате
    val listOfNumbers: List<Int>    //Лист чисел с вариантами неправильных ответов
)