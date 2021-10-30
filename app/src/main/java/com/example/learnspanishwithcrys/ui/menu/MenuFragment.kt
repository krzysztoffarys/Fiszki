package com.example.learnspanishwithcrys.ui.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.learnspanishwithcrys.R
import com.example.learnspanishwithcrys.databinding.MenuFragmentBinding

class MenuFragment : Fragment(R.layout.menu_fragment) {

    private lateinit var binding: MenuFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MenuFragmentBinding.bind(view)

        binding.cvMatch.setOnClickListener {
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToStartMatchFragment()
            )
        }

        binding.cvWrite.setOnClickListener {
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToWriteFragment()
            )
        }
    }

}