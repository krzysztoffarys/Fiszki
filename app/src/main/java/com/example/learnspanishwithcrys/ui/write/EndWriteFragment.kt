package com.example.learnspanishwithcrys.ui.write

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.learnspanishwithcrys.R
import com.example.learnspanishwithcrys.databinding.EndWriteFragmentBinding

class EndWriteFragment : Fragment(R.layout.end_write_fragment) {

    private lateinit var binding: EndWriteFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EndWriteFragmentBinding.bind(view)

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
    }

}