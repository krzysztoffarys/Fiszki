package com.example.learnspanishwithcrys.ui.menu

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnspanishwithcrys.R
import com.example.learnspanishwithcrys.adapters.WordAdapter
import com.example.learnspanishwithcrys.databinding.MenuFragmentBinding
import com.example.learnspanishwithcrys.ui.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MenuFragment : Fragment(R.layout.menu_fragment) {

    private lateinit var binding: MenuFragmentBinding
    private lateinit var wordAdapter: WordAdapter
    private val viewModel: SharedViewModel by activityViewModels()
    @Inject
    lateinit var auth: FirebaseAuth
    @Inject
    lateinit var mediaPlayer: MediaPlayer


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MenuFragmentBinding.bind(view)
        setupRecyclerView()

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

        binding.cvFlashcard.setOnClickListener {
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToFlashcardFragment()
            )
        }

        binding.cvLogout.setOnClickListener {
            auth.signOut()
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToAuthActivity()
            )
        }

        wordAdapter.setOnSoundItemClickListener { url ->
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }
    }

    private fun setupRecyclerView() = binding.rv.apply {
        wordAdapter = WordAdapter()
        adapter = wordAdapter
        layoutManager = LinearLayoutManager(requireContext())
        wordAdapter.words = viewModel.words
    }

}