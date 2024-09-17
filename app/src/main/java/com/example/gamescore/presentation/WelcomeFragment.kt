package com.example.gamescore.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gamescore.R
import com.example.gamescore.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding: FragmentWelcomeBinding
        get() = _binding ?: throw RuntimeException("Переменная $_binding равна null!")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonUnderstand.setOnClickListener {
            launchChooseLevelFragment()
        }
    }

    private fun launchChooseLevelFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, ChooseLevelFragment.newInstance())
            .addToBackStack(ChooseLevelFragment.NAME)
            .commit()
    }

    companion object {

        fun newInstance(): WelcomeFragment {
            return WelcomeFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}