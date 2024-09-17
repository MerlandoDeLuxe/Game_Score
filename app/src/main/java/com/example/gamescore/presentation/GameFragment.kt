package com.example.gamescore.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.gamescore.R
import com.example.gamescore.databinding.FragmentGameBinding
import com.example.gamescore.domain.entity.GameResult
import com.example.gamescore.domain.entity.Level

class GameFragment : Fragment() {
    private val TAG = "GameFragment"

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("${TAG}: Переменная _binding равна null!")

    private lateinit var level: Level

    private val textViewOptions by lazy {   //Если без lazy, то инициализация будет сразу
        mutableListOf<TextView>().apply {   //до вызова onViewCreated, т.е. вью еще не существует
            with(binding) {
                add(textViewOption1)
                add(textViewOption2)
                add(textViewOption3)
                add(textViewOption4)
                add(textViewOption5)
                add(textViewOption6)
            }
        }
    }

    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(this)
            .get(GameViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            parseArgs()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeAllElements()

        with(binding) {
            textViewBeginButton.visibility = TextView.VISIBLE
            textViewBeginButton.setOnClickListener {
                if (pressBegin()) {
                    viewModel.startGame(level)
                    observeViewModel()
                    setupOnClickListeners()
                }
            }
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                retryGame()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun observeViewModel() {
        setNewQuestion()
        setTimerText()
        setTimerColor()
        setProgressAnswers()
        updateTextViewLabelProgress()
        updateProgressBar()
        finishGame()
        updateTextViewColor()
        changeProgressBarColor()
        changeSecondaryProgressBarStep()
        finishGame()
    }

    private fun setNewQuestion() {
        viewModel.question.observe(viewLifecycleOwner) {
            with(binding) {
                textViewMark.text = it.mark
                textViewSum.text = it.sum.toString()
                textViewVisibleNumber.text = it.visibleNumber.toString()

                for (i in 0..textViewOptions.size - 1) {
                    textViewOptions.get(i).text = it.listOfNumbers.get(i).toString()
                }
            }
        }
    }

    private fun finishGame() {
        viewModel.gameResult.observe(viewLifecycleOwner) {
            launchGameFinishedFragment(it)
        }
    }

    private fun updateTextViewLabelProgress() {
        viewModel.progressAnswers.observe(viewLifecycleOwner) {
            binding.textViewRightAnswers.text = it
        }
    }

    private fun updateProgressBar() {
        viewModel.percentOfRightAnswers.observe(viewLifecycleOwner) {
            binding.progressBar.setProgress(it, true)
        }
    }

    private fun updateTextViewColor() {
        viewModel.enoughCountOfRightAnswers.observe(viewLifecycleOwner) {
            binding.textViewRightAnswers.setTextColor(getColorByState(it))
        }
    }

    private fun changeProgressBarColor() {
        viewModel.enoughPercentOfRightAnswers.observe(viewLifecycleOwner) {
            val color = getColorByState(it)
            binding.progressBar.progressTintList = ColorStateList.valueOf(color)
        }
    }

    private fun changeSecondaryProgressBarStep() {
        viewModel.minPercentForCompleteLevel.observe(viewLifecycleOwner) {
            binding.progressBar.secondaryProgress = it
        }
    }

    //Метод получения цветов
    private fun getColorByState(goodState: Boolean): Int {
        val colorGreenResId = android.R.color.holo_green_light
        val colorRedResId = android.R.color.holo_red_light
        return if (goodState) {
            ContextCompat.getColor(requireContext(), colorGreenResId)
        } else {
            ContextCompat.getColor(requireContext(), colorRedResId)
        }
    }

    private fun setTimerText() {
        viewModel.formattedTimerText.observe(viewLifecycleOwner) {
            binding.textViewTimer.text = it
        }
    }

    private fun setTimerColor(){
        viewModel.timerColor.observe(viewLifecycleOwner) {
            binding.textViewTimer.setTextColor(getColorByState(it))
        }
    }

    private fun setProgressAnswers(){
        viewModel.progressAnswers.observe(viewLifecycleOwner) {
            binding.textViewRightAnswers.text = it
        }
    }

    private fun setupOnClickListeners() {
        for (option in textViewOptions) {
            option.setOnClickListener {
                viewModel.chooseAnswer(option.text.toString().toInt())
            }
        }
    }

    private fun pressBegin(): Boolean {
        binding.textViewBeginButton.visibility = TextView.GONE
        return true
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, GameFinishedFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }


    private fun parseArgs() {
        requireArguments().getParcelable<Level>(KEY_LEVEL)?.let {
            level = it
        }
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(
            NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    fun initializeAllElements() {
    }

    companion object {
        const val NAME = "GameFragment"
        private const val KEY_LEVEL = "level"

        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}