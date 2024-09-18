package com.example.gamescore.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gamescore.R
import com.example.gamescore.databinding.FragmentChooseLevelBinding
import com.example.gamescore.domain.entity.Level

class ChooseLevelFragment : Fragment() {
    private val TAG = "ChooseLevelFragment"
    private var _binding: FragmentChooseLevelBinding? = null
    private val binding: FragmentChooseLevelBinding
        get() = _binding ?: throw RuntimeException("${TAG}: Переменная $_binding равна null!")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
    }

    private fun bindViews() {
        with(binding) {
            buttonTestLevel.setOnClickListener {
                launchGameFragment(Level.TEST)
            }

            buttonEasyLevel.setOnClickListener {
                launchGameFragment(Level.EASY)
            }

            buttonMediumLevel.setOnClickListener {
                launchGameFragment(Level.MEDIUM)
            }

            buttonHardLevel.setOnClickListener {
                launchGameFragment(Level.HARD)
            }
        }
    }

    private fun launchGameFragment(level: Level) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, GameFragment.newInstance(level))
            .addToBackStack(GameFragment.NAME)
            .commit()
    }

    companion object {

        const val NAME = "ChooseLevelFragment"

        fun newInstance(): ChooseLevelFragment {
            return ChooseLevelFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}