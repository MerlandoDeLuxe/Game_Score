package com.example.gamescore.presentation

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gamescore.R
import com.example.gamescore.databinding.FragmentGameBinding
import com.example.gamescore.domain.entity.GameResult
import com.example.gamescore.domain.entity.Level

class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("Переменная _binding равна null!")

    private lateinit var level: Level

    private lateinit var viewModel: GameViewModel
    private lateinit var viewModelFactory: GameViewModelFactory

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
                    val question = viewModel.generateQuestion()
                    textViewSum.text = question.sum.toString()
                    textViewLeftNumber.text = question.visibleNumber.toString()
                    textViewOption1.text = question.listOfNumbers.get(0).toString()
                }
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

    fun initializeAllElements() {
        viewModelFactory = GameViewModelFactory(level)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)
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