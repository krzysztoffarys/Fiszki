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
import com.example.learnspanishwithcrys.adapters.CategoryAdapter
import com.example.learnspanishwithcrys.adapters.EndWordAdapter
import com.example.learnspanishwithcrys.adapters.WordAdapter
import com.example.learnspanishwithcrys.data.model.Category
import com.example.learnspanishwithcrys.data.model.Word
import com.example.learnspanishwithcrys.databinding.MenuFragmentBinding
import com.example.learnspanishwithcrys.other.Resource
import com.example.learnspanishwithcrys.ui.SharedViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MenuFragment : Fragment(R.layout.menu_fragment) {

    private lateinit var binding: MenuFragmentBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
    @Inject
    lateinit var auth: FirebaseAuth
    @Inject
    lateinit var mediaPlayer: MediaPlayer


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

        subscribeToObservers()
        setupRecyclerView()
    }


    private fun setupRecyclerView() = binding.rv.apply {
        categoryAdapter = CategoryAdapter(listOf(
            Category("Charakter i osobowośc", 12),
            Category("Czas", 38),
            Category("Czesci ciała", 25),
            Category("Dom", 34),

        ))
        adapter = categoryAdapter
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun subscribeToObservers() {
        sharedViewModel.wordsStatus.observe(viewLifecycleOwner, { result ->
            when(result.status) {
                Resource.Status.SUCCESS -> {
                    binding.tvTerms.text = "${result.data!!.size} słówek"
                    sharedViewModel.words = result.data!!
                }
                Resource.Status.LOADING -> {
                }
                Resource.Status.ERROR -> {
                    val message = result.message ?: context?.getString(R.string.unknown_error)
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}