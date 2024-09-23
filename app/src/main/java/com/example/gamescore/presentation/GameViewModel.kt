package com.example.gamescore.presentation

import android.app.Application
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gamescore.R
import com.example.gamescore.data.GameRepositoryImpl
import com.example.gamescore.domain.entity.GameResult
import com.example.gamescore.domain.entity.GameSettings
import com.example.gamescore.domain.entity.Level
import com.example.gamescore.domain.entity.Question
import com.example.gamescore.domain.usecases.GenerateQuestionUseCase
import com.example.gamescore.domain.usecases.GetGameSettingsUseCase
import java.util.Locale
import java.util.concurrent.TimeUnit

class GameViewModel(private val level: Level, private val application: Application) :
    ViewModel() {
    private val TAG = "GameViewModel"
    private val repository = GameRepositoryImpl

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private var maxSumValue = 0
    private lateinit var gameSettings: GameSettings

    private var timer: CountDownTimer? = null

    private val _formattedTimerText = MutableLiveData<String>()//Красивый вид отформатированного
    val formattedTimerText: LiveData<String>                    //текста таймера
        get() = _formattedTimerText

    private val _timerColor =
        MutableLiveData<Boolean>()//Чтобы изменить текст таймера перед истечением
    val timerColor: LiveData<Boolean>
        get() = _timerColor

    private val _question =
        MutableLiveData<Question>()  //Прилетает вопрос и парсим его во фрагменте
    val question: LiveData<Question>
        get() = _question

    private val _percentOfRightAnswers = MutableLiveData<Int>() //Для передачи в ProgressBar
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    private val _progressAnswers = MutableLiveData<String>()    //Изенение текстового поля
    val progressAnswers: LiveData<String>               //количества правильных ответов в TextView
        get() = _progressAnswers

    private val _enoughCountOfRightAnswers = MutableLiveData<Boolean>() //Для изменения текста
    val enoughCountOfRightAnswers: LiveData<Boolean>           // достаточного количества ответов
        get() = _enoughCountOfRightAnswers

    private val _enoughPercentOfRightAnswers = MutableLiveData<Boolean>()//Для изменения цвета фона
    val enoughPercentOfRightAnswers: LiveData<Boolean>          //ProgressBar основной шкалы
        get() = _enoughPercentOfRightAnswers

    private val _minPercentForCompleteLevel = MutableLiveData<Int>()//Для изменения цвета фона
    val minPercentForCompleteLevel: LiveData<Int>           //ProgressBar вторичной шкалы
        get() = _minPercentForCompleteLevel

    private val _gameResult = MutableLiveData<GameResult>()//Если сюда упали результаты,
    val gameResult: LiveData<GameResult>                   // значит игра окончена
        get() = _gameResult

    private var countOfRightAnswers = 0
    private var countOfQuestions = 0
    private var percentOfRightAnswersForCreateGameResult = 0

    init {
        getGameSettings()
        updateProgress()
        initializeTimer()
    }

    private fun initializeTimer() {
        _formattedTimerText.value = formatTime(convertSecondsToLongForTimer())
        Log.d(TAG, "initializeTimer: _formattedTimerText = ${_formattedTimerText.value}")
    }

    fun startGame() {
        startTimer(convertSecondsToLongForTimer())
        generateQuestion()
    }

    fun chooseAnswer(number: Int) {
        checkAnswerForCorrect(number)
        updateProgress()
        generateQuestion()
    }

    private fun updateProgress() {
        percentOfRightAnswersForCreateGameResult = calculatePercentOfRightAnswers()
        _percentOfRightAnswers.value = percentOfRightAnswersForCreateGameResult
        _progressAnswers.value = String.format(
            application.resources.getString(R.string.count_right_answers),
            countOfRightAnswers,
            gameSettings.minCountOfRightAnswers
        )

        _enoughCountOfRightAnswers.value =
            countOfRightAnswers >= gameSettings.minCountOfRightAnswers
        _enoughPercentOfRightAnswers.value =
            percentOfRightAnswersForCreateGameResult >= gameSettings.minPercentOfRightAnswer
    }

    private fun calculatePercentOfRightAnswers(): Int {
        if (countOfQuestions == 0) {
            return 0
        }
        return (countOfRightAnswers / countOfQuestions.toDouble() * 100).toInt()
    }


    private fun checkAnswerForCorrect(number: Int) {
        val rightAnswer = _question.value?.rightAnswer
        if (number == rightAnswer) {
            countOfRightAnswers++
        }
        countOfQuestions++
    }

    private fun getGameSettings() {
        gameSettings = getGameSettingsUseCase(level)
        maxSumValue = gameSettings.maxSumValue
        _minPercentForCompleteLevel.value = gameSettings.minPercentOfRightAnswer
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(maxSumValue)
    }

    private fun finishGame() {
        //Явные параметры указал просто для читаемости кода, они необязательны
        val gameResult = GameResult(
            winner = enoughCountOfRightAnswers.value == true
                    && enoughPercentOfRightAnswers.value == true,
            countOfRightAnswers = countOfRightAnswers,
            countOfQuestions = countOfQuestions,
            percentOfRightAnswers = percentOfRightAnswersForCreateGameResult,
            gameSettings = gameSettings
        )
        _gameResult.value = gameResult

        Log.d(TAG, "finishGame: gameResult: $gameResult")
    }

    private fun convertSecondsToLongForTimer(): Long {
        return gameSettings.gameTimeInSeconds.toLong() * MILLIS_IN_SECONDS
    }

    private fun startTimer(longSeconds: Long) {
        timer = object : CountDownTimer(longSeconds, MILLIS_IN_SECONDS) {
            override fun onTick(millis: Long) {
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millis)
                _formattedTimerText.value = formatTime(millis)

                if (seconds < 10) {
                    _timerColor.value = false
                } else {
                    _timerColor.value = true
                }
            }

            override fun onFinish() {
                finishGame()
            }
        }.start()
    }

    private fun formatTime(millis: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))

        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    override fun onCleared() {
        timer?.cancel()
    }

    companion object {
        private const
        val MILLIS_IN_SECONDS = 1000L
        private const val TIMER_INTERVAL = 100L
    }
}