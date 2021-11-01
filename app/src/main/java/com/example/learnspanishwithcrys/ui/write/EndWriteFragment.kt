package com.example.learnspanishwithcrys.ui.write

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnspanishwithcrys.R
import com.example.learnspanishwithcrys.adapters.WriteWordAdapter
import com.example.learnspanishwithcrys.databinding.EndWriteFragmentBinding
import com.example.learnspanishwithcrys.ui.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class EndWriteFragment : Fragment(R.layout.end_write_fragment) {

    private lateinit var binding: EndWriteFragmentBinding
    private lateinit var writeWordAdapter: WriteWordAdapter
    private val viewModel: SharedViewModel by activityViewModels()
    @Inject
    lateinit var mediaPlayer: MediaPlayer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EndWriteFragmentBinding.bind(view)
        setupTextView()
        setupRecyclerView()
        binding.btnBackToMenu.setOnClickListener {
            findNavController().navigate(
                EndWriteFragmentDirections.actionEndWriteFragmentToMenuFragment()
            )
        }

        binding.btnRestart.setOnClickListener {
            findNavController().navigate(
                EndWriteFragmentDirections.actionEndWriteFragmentToWriteFragment()
            )
        }
        writeWordAdapter.setOnSoundItemClickListener { url ->
            Timber.d(url + "halo")
            mediaPlayer.stop()
            mediaPlayer.setDataSource(url)
            mediaPlayer.start()

        }
        writeWordAdapter.answers = viewModel.writeAnswers
        writeWordAdapter.words = viewModel.words
    }
    private fun setupTextView() {
        val text = SpannableStringBuilder()
        val span1 = SpannableString(viewModel.writeCorrectAnswers.toString())
        span1.setSpan(ForegroundColorSpan(Color.GREEN), 0, span1.length, 0)
        val span2 = SpannableString(viewModel.writeIncorrectAnswers.toString())
        span2.setSpan(ForegroundColorSpan(Color.RED), 0, span2.length, 0)
        text.append(span1, " - correct answers\n", span2, " - incorrect answers")
        binding.tvAnswers.text = text
    }

    private fun setupRecyclerView() = binding.rv.apply {
        writeWordAdapter = WriteWordAdapter()
        adapter = writeWordAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }
}