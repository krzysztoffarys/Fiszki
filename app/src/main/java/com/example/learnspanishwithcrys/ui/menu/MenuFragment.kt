package com.example.learnspanishwithcrys.ui.menu

import android.media.MediaPlayer
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnspanishwithcrys.R
import com.example.learnspanishwithcrys.adapters.CategoryAdapter
import com.example.learnspanishwithcrys.data.model.Category
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
        subscribeToObservers()
        setupRecyclerView()

        binding.cvMatch.setOnClickListener {
            if (sharedViewModel.isLoading) {
                return@setOnClickListener
            }
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToStartMatchFragment()
            )
        }

        binding.cvWrite.setOnClickListener {
            if (sharedViewModel.isLoading) {
                return@setOnClickListener
            }
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToStartWriteFragment()
            )
        }

        binding.cvFlashcard.setOnClickListener {
            if (sharedViewModel.isLoading) {
                return@setOnClickListener
            }
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToFlashcardFragment()
            )
        }

        binding.cvLogout.setOnClickListener {
            auth.signOut()
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.writeFragment, true)
                .build()
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToAuthFragment(),
                navOptions
            )
        }

        categoryAdapter.setOnItemClickListener { category ->
            sharedViewModel.loadNewCategory(category)
        }
    }


    private fun setupRecyclerView() = binding.rv.apply {
        categoryAdapter = CategoryAdapter()
        adapter = categoryAdapter
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun subscribeToObservers() {
        sharedViewModel.wordsStatus.observe(viewLifecycleOwner, { result ->
            when(result.status) {
                Resource.Status.SUCCESS -> {
                    sharedViewModel.isLoading = false
                    binding.progressBar.visibility = View.GONE
                    binding.tvTerms.text = "${result.data!!.size} słówek"
                    sharedViewModel.words = result.data!!
                }
                Resource.Status.ERROR -> {
                    sharedViewModel.isLoading = false
                    binding.progressBar.visibility = View.GONE
                    val message = result.message ?: context?.getString(R.string.unknown_error)
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
                Resource.Status.LOADING -> {
                    sharedViewModel.isLoading = true
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })

        sharedViewModel.curCategory.observe(viewLifecycleOwner, { category ->
            binding.tvTitle.text = category.name
            categoryAdapter.categories =  sharedViewModel.provideCategories()
        })
    }

}