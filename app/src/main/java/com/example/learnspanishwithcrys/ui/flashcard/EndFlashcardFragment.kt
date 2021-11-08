package com.example.learnspanishwithcrys.ui.flashcard

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnspanishwithcrys.R
import com.example.learnspanishwithcrys.adapters.EndWordAdapter
import com.example.learnspanishwithcrys.databinding.EndFlashcardFragmentBinding
import com.example.learnspanishwithcrys.ui.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EndFlashcardFragment : Fragment(R.layout.end_flashcard_fragment) {

    private lateinit var binding: EndFlashcardFragmentBinding
    private lateinit var endWordAdapter: EndWordAdapter
    private val viewModel: SharedViewModel by activityViewModels()
    @Inject
    lateinit var mediaPlayer: MediaPlayer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EndFlashcardFragmentBinding.bind(view)
        setupTextView()
        setupRecyclerView()
        binding.btnBackToMenu.setOnClickListener {
            findNavController().navigate(
                EndFlashcardFragmentDirections.actionEndFlashcardFragmentToMenuFragment()
            )
        }

        binding.btnRestart.setOnClickListener {
            findNavController().navigate(
                EndFlashcardFragmentDirections.actionEndFlashcardFragmentToFlashcardFragment()
            )
        }
        endWordAdapter.setOnSoundItemClickListener { url ->
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }
    }


    private fun setupTextView() {
        val text = SpannableStringBuilder()
        val span1 = SpannableString(viewModel.flashcardCorrectAnswers.toString())
        span1.setSpan(ForegroundColorSpan(Color.GREEN), 0, span1.length, 0)
        val span2 = SpannableString(viewModel.flashcardIncorrectAnswers.toString())
        span2.setSpan(ForegroundColorSpan(Color.RED), 0, span2.length, 0)
        text.append(span1, " - known words\n", span2, " - unknown words")
        binding.tvAnswers.text = text
    }

    private fun setupRecyclerView() = binding.rv.apply {
        endWordAdapter = EndWordAdapter()
        adapter = endWordAdapter
        layoutManager = LinearLayoutManager(requireContext())
        endWordAdapter.answers = viewModel.flashcardAnswers
        endWordAdapter.words = viewModel.words
    }

}