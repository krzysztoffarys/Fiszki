package com.example.learnspanishwithcrys.ui.match

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.learnspanishwithcrys.R
import com.example.learnspanishwithcrys.databinding.EndMatchFragmentBinding

class EndMatchFragment : Fragment(R.layout.end_match_fragment) {
    val args: EndMatchFragmentArgs by navArgs()
    private lateinit var binding: EndMatchFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EndMatchFragmentBinding.bind(view)
        if (args.victory) {
            val time = args.time
            binding.tv.text = "You won with time $time!!!"
            binding.tv.setTextColor(Color.GREEN)
        } else {
            binding.tv.text = "You lose. Wanna try again?"
            binding.tv.setTextColor(Color.RED)
        }

        binding.btnRestart.setOnClickListener {
            findNavController().navigate(
                EndMatchFragmentDirections.actionEndMatchFragmentToMatchFragment()
            )
        }

        binding.btnBackToMenu.setOnClickListener {
            TODO()
        }
    }
}