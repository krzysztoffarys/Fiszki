package com.example.learnspanishwithcrys.ui.match

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.learnspanishwithcrys.R
import com.example.learnspanishwithcrys.databinding.MatchFragmentBinding
import com.example.learnspanishwithcrys.other.GameStatus
import com.example.learnspanishwithcrys.ui.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchFragment : Fragment(R.layout.match_fragment) {

    private lateinit var binding: MatchFragmentBinding
    private val viewModel: MatchViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MatchFragmentBinding.bind(view)
        viewModel.setWords(sharedViewModel.words)
        viewModel.prepareWords()
        setButtons()
        subscribeToObservers()

    }

    private fun subscribeToObservers() {
        viewModel.time.observe(viewLifecycleOwner, { title ->
            binding.toolbar.title = title
        })

        viewModel.colorTitle.observe(viewLifecycleOwner, { color ->
            binding.toolbar.setTitleTextColor(color)
        })

        viewModel.gameStatus.observe(viewLifecycleOwner, { Game ->
            when(Game.status) {
                GameStatus.WIN -> {
                    val time = Game.message ?: ""
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.matchFragment, true)
                        .build()
                    findNavController().navigate(
                        MatchFragmentDirections.actionMatchFragmentToEndMatchFragment(true, time),
                        navOptions
                    )
                }
                GameStatus.LOSE -> {
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.matchFragment, true)
                        .build()
                    findNavController().navigate(
                        MatchFragmentDirections.actionMatchFragmentToEndMatchFragment(false),
                        navOptions
                    )
                }
                GameStatus.ONGOING -> {
                    //game still going
                }
            }

        })
    }

    private fun setButtons() {
        val listOfTextViews = listOf(
            binding.tv0,
            binding.tv1,
            binding.tv2,
            binding.tv3,
            binding.tv4,
            binding.tv5,
            binding.tv6,
            binding.tv7,
            binding.tv8,
            binding.tv9,
            binding.tv10,
            binding.tv11
        )
        for (i in listOfTextViews.indices) {
            listOfTextViews[i].setTextColor(Color.BLACK)
            listOfTextViews[i].text = viewModel.words[i]
            listOfTextViews[i].setOnClickListener(viewModel.onClickListener)
        }
    }

}