package com.example.learnspanishwithcrys.ui.menu

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnspanishwithcrys.R
import com.example.learnspanishwithcrys.adapters.WordAdapter
import com.example.learnspanishwithcrys.databinding.MenuFragmentBinding
import com.example.learnspanishwithcrys.other.Resource
import com.example.learnspanishwithcrys.ui.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MenuFragment : Fragment(R.layout.menu_fragment) {

    private lateinit var binding: MenuFragmentBinding
    private lateinit var wordAdapter: WordAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
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
                MenuFragmentDirections.actionMenuFragmentToAuthFragment()
            )
        }

        wordAdapter.setOnSoundItemClickListener { url ->
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }

        subscribeToObservers()
    }

    private fun setupRecyclerView() = binding.rv.apply {
        wordAdapter = WordAdapter()
        adapter = wordAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun subscribeToObservers() {
        sharedViewModel.wordsStatus.observe(viewLifecycleOwner, { result ->
            when(result.status) {
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    wordAdapter.words = result.data!!
                    binding.tvTerms.text = "${result.data.size} słówek"
                    sharedViewModel.words = result.data
                }
                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    val message = result.message ?: context?.getString(R.string.unknown_error)
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}