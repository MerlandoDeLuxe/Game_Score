package com.example.gamescore.domain.entity

data class GameResult(
    val winner: Boolean,            //Победили мы или нет
    val countOfRightAnswers: Int,   //Количество даных правильных ответов
    val countOfQuestions: Int,      //Общее количество вопросов, на которое ответил пользователь,
                                    // чтобы посчитать процент правильных ответов
    val gameSettings: GameSettings  //Для отображения правильных результатов на экране результатов,
                                    //например сколько правильных ответов должно было быть
                                    //на данном режиме сложности
)