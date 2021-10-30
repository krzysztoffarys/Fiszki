package com.example.learnspanishwithcrys.ui.match

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.learnspanishwithcrys.R
import com.example.learnspanishwithcrys.databinding.StartMatchFragmentBinding

class StartMatchFragment : Fragment(R.layout.start_match_fragment) {
    private lateinit var binding: StartMatchFragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = StartMatchFragmentBinding.bind(view)
        binding.btnStart.setOnClickListener {
            findNavController().navigate(
                StartMatchFragmentDirections.actionStartMatchFragmentToMatchFragment()
            )
        }
    }
}