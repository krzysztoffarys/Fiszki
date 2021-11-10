package com.example.learnspanishwithcrys.ui.write

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.learnspanishwithcrys.R
import com.example.learnspanishwithcrys.databinding.WriteFragmentBinding
import com.example.learnspanishwithcrys.other.GameStatus
import com.example.learnspanishwithcrys.ui.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteFragment : Fragment(R.layout.write_fragment) {

    private lateinit var binding: WriteFragmentBinding
    private val viewModel: WriteViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = WriteFragmentBinding.bind(view)



        viewModel.words = sharedViewModel.writeWords
        subscribeToObserves()
        binding.et.setOnEditorActionListener { editText, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val text = editText.text.toString()
                viewModel.checkAnswer(text)
                editText.text = ""
                return@setOnEditorActionListener true
            }
            false
        }
        binding.btnDontKnow.setOnClickListener(viewModel.onClickListener)
    }


   private fun subscribeToObserves() {
        viewModel.selectedWordId.observe(viewLifecycleOwner, { id ->
            if (id < viewModel.words.size) {
                binding.tvDisplayAnswer.text = viewModel.words[id].spanish
                binding.tvWord.text = viewModel.words[id].polish
            } else {
                sharedViewModel.writeCorrectAnswers = viewModel.correctAnswers
                sharedViewModel.writeIncorrectAnswers = viewModel.incorrectAnswers
                sharedViewModel.writeAnswers = viewModel.answers
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.writeFragment, true)
                    .build()
                findNavController().navigate(
                    WriteFragmentDirections.actionWriteFragmentToEndWriteFragment(),
                    navOptions
                )
            }
        })

       viewModel.answerStatus.observe(viewLifecycleOwner, { Game ->
           when(Game.status) {
               GameStatus.WIN -> {
                   binding.ivStatus.visibility = View.VISIBLE
                   Glide.with(binding.ivStatus)
                       .load(R.drawable.ic_check)
                       .into(binding.ivStatus)

               }
               GameStatus.LOSE -> {
                   binding.ivStatus.visibility = View.VISIBLE
                   Glide.with(binding.ivStatus)
                       .load(R.drawable.ic_cross)
                       .into(binding.ivStatus)
               }
               GameStatus.ONGOING -> {
                   binding.ivStatus.visibility = View.GONE
               }
           }
       })

       viewModel.buttonVisibility.observe(viewLifecycleOwner, { visibility ->
           binding.btnDontKnow.visibility = visibility
       })

       viewModel.answerVisibility.observe(viewLifecycleOwner, { visibility ->
           binding.tvCorrectAnswer.visibility = visibility
           binding.tvDisplayAnswer.visibility = visibility
       })
   }

}