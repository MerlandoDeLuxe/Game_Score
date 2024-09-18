package com.example.gamescore.presentation

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.gamescore.R
import com.example.gamescore.databinding.FragmentFinishedFameBinding
import com.example.gamescore.domain.entity.GameResult

class GameFinishedFragment : Fragment() {
    private val TAG = "GameFinishedFragment"
    private var _binding: FragmentFinishedFameBinding? = null
    private val binding: FragmentFinishedFameBinding
        get() = _binding ?: throw RuntimeException("${TAG}: Переменная $_binding равна null!")

    private lateinit var gameResult: GameResult

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedFameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSmileImageResId()
        bindViews()
        setOnClickListeners()
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                retryGame()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun bindViews() {
        with(binding) {
            binding.imageViewResultEmoji.setImageResource(getSmileImageResId())

            var result = String.format(
                requireContext().resources.getString(R.string.count_required_answers),
                gameResult.gameSettings.minCountOfRightAnswers.toString()
            )
            textViewCountRequiredAnswers.text = result

            result = String.format(
                requireContext().resources.getString(R.string.your_result),
                gameResult.countOfRightAnswers.toString()
            )
            textViewYourResult.text = result

            result = String.format(
                requireContext().resources.getString(R.string.required_percent_right_answers),
                gameResult.gameSettings.minPercentOfRightAnswer.toString()
            )
            textViewRequiredPercentRightAnswers.text = result

            result = String.format(
                requireContext().resources.getString(R.string.your_percent_result),
                gameResult.percentOfRightAnswers.toString()
            )
            textViewYourPercentResult.text = result
        }
    }

    private fun getSmileImageResId(): Int {
        return if (gameResult.winner) {
            R.drawable.emoji_smile
        } else {
            R.drawable.emoji_sad
        }
    }

    private fun setOnClickListeners() {
        binding.buttonTryAgain.setOnClickListener { retryGame() }
    }

    private fun parseArgs() {
        requireArguments().getParcelable<GameResult>(KEY_GAME_RESULT)?.let {
            gameResult = it
        }
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    companion object {

        private const val KEY_GAME_RESULT = "game_result"

        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}