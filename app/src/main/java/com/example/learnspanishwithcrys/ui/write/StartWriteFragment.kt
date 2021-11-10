package com.example.learnspanishwithcrys.ui.write

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.learnspanishwithcrys.R
import com.example.learnspanishwithcrys.databinding.StartWriteFragmentBinding
import com.example.learnspanishwithcrys.ui.SharedViewModel

class StartWriteFragment : Fragment(R.layout.start_write_fragment) {

    private lateinit var binding: StartWriteFragmentBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var words = 5

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = StartWriteFragmentBinding.bind(view)
        setSeekBar()
        sharedViewModel.newWrite()
        binding.cvStart.setOnClickListener {
            sharedViewModel.writeWords = sharedViewModel.words.shuffled().subList(0, words)
            findNavController().navigate(
                StartWriteFragmentDirections.actionStartWriteFragmentToWriteFragment()
            )
        }
    }

    private fun setSeekBar() {
        binding.seekBar.min = 3
        binding.seekBar.progress = words
        binding.tv.text = "Nauczysz się $words nowych słów"
        binding.seekBar.max = sharedViewModel.words.size
        binding.seekBar.setOnSeekBarChangeListener( object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tv.text = "Nauczysz się ${progress} nowych słów"
                words = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }
}