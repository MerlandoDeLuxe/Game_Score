package com.example.gamescore.presentation

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.window.OnBackInvokedCallback
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.gamescore.R
import com.example.gamescore.databinding.FragmentFinishedFameBinding
import com.example.gamescore.domain.entity.GameResult
import java.util.Locale

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
        with(binding) {
            textViewYourResult.text = String.format(Locale.getDefault(), "%d", gameResult.countOfRightAnswers)
            buttonTryAgain.setOnClickListener { retryGame() }

            var picDawable: Drawable? = null

            if (gameResult.winner){
                picDawable = context?.let { AppCompatResources.getDrawable(it,R.drawable.emoji_smile) }

            }else{
                picDawable = context?.let { AppCompatResources.getDrawable(it,R.drawable.emoji_sad) }
            }
            imageViewResultEmoji.setImageDrawable(picDawable)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                retryGame()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
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