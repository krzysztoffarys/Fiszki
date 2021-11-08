package com.example.learnspanishwithcrys.ui.flashcard

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipDescription
import android.content.SharedPreferences
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.learnspanishwithcrys.R
import com.example.learnspanishwithcrys.databinding.FlashcardFragmentBinding
import com.example.learnspanishwithcrys.other.Constants.ANIMATION_DURATION_FLASHCARD
import com.example.learnspanishwithcrys.ui.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.absoluteValue

@AndroidEntryPoint
class FlashcardFragment : Fragment(R.layout.flashcard_fragment) {

    private lateinit var binding: FlashcardFragmentBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val viewModel: FlashcardViewModel by viewModels()
    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FlashcardFragmentBinding.bind(view)
        viewModel.words = sharedViewModel.words
        sharedViewModel.newFlashcard()


        setupLayout()
        subscribeToObservers()
    }


    private fun setupLayout() {

        when (viewModel.selectedOption) {
            0 -> {
                binding.tvCard.text =  viewModel.words[0].polish
            }
            1 -> {
                binding.tvCard.text =  viewModel.words[0].spanish
            }
            else -> {
                binding.tvCard.text = "${viewModel.words[0].polish}\n\n${viewModel.words[0].spanish}"
            }
        }


        binding.tvCard.setOnClickListener {
            if (viewModel.selectedWordId.value!! > viewModel.words.size) {
                return@setOnClickListener
            }

            if (viewModel.selectedOption == 2) {
                return@setOnClickListener
            }

            if (viewModel.isPolish) {
                rotate(viewModel.words[viewModel.selectedWordId.value!!].spanish)
            } else {
                rotate(viewModel.words[viewModel.selectedWordId.value!!].polish)
            }
            viewModel.isPolish = !viewModel.isPolish
        }

        binding.tvCard.setOnLongClickListener {
            val clipText = ""
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)

            val dragShadowBuilder = View.DragShadowBuilder(it)
                it.startDragAndDrop(data, dragShadowBuilder, it, View.DRAG_FLAG_OPAQUE)
            it.visibility = View.INVISIBLE
            true
        }
        var wordStatus = true
        val dragListener = View.OnDragListener { view, event ->
            when(event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    view.invalidate()
                    true
                }
                DragEvent.ACTION_DRAG_LOCATION -> {
                    val width = view.width / 2
                    binding.ivStatus.alpha = ((width) - event.x).absoluteValue / width / 2
                    if (width - event.x > 0) {
                        wordStatus = true
                        binding.ivStatus.setBackgroundResource(R.drawable.ic_check)
                    } else {
                        wordStatus = false
                        binding.ivStatus.setBackgroundResource(R.drawable.ic_cross)
                    }

                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    view.invalidate()
                    true
                }
                DragEvent.ACTION_DROP -> {
                    val v = event.localState as View
                    v.visibility = View.VISIBLE
                    v.invalidate()
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    val v = event.localState as View
                    v.visibility = View.VISIBLE
                    binding.ivStatus.background = null
                    viewModel.isTheWordKnown(wordStatus)
                    viewModel.nextWord()
                    view.invalidate()
                    true
                }
                else -> false
            }
        }

        binding.clCenter.setOnDragListener(dragListener)

        binding.progressBar.apply {
            max = sharedViewModel.words.size
            min = 0
            progress = 0
        }

        //dialog options

        binding.btnOptions.setOnClickListener {
            var selectedOption = viewModel.selectedOption
            AlertDialog.Builder(requireContext(), R.style.AlertDialogStyle)
                .setTitle("Choose front")
                .setSingleChoiceItems(viewModel.options, selectedOption) { _, i ->
                    selectedOption = i
                }
                .setPositiveButton("Accept") { _, _ ->
                    sharedPref.edit().putInt(
                        "flashcardOption", selectedOption
                    ).apply()
                    viewModel.selectedOption = selectedOption
                    viewModel.optionsChange()
                }
                .setNegativeButton("Decline") { _, _ -> }
                .create()
                .show()
        }
    }

    private fun rotate(title: String) {
        ObjectAnimator.ofFloat(binding.tvCard, "rotationY", 0F, 90F)
            .setDuration(ANIMATION_DURATION_FLASHCARD)
            .start()

        viewLifecycleOwner.lifecycleScope.launch{
            delay(ANIMATION_DURATION_FLASHCARD)
            binding.tvCard.text = title
            ObjectAnimator.ofFloat(binding.tvCard, "rotationY", 270F, 360F)
                .setDuration(ANIMATION_DURATION_FLASHCARD)
                .start()
        }

    }

    private fun subscribeToObservers() {
        viewModel.selectedWordId.observe(viewLifecycleOwner, {
            if (it < viewModel.words.size) {
                when (viewModel.selectedOption) {
                    0 -> {
                        binding.tvCard.text =  viewModel.words[it].polish
                    }
                    1 -> {
                        binding.tvCard.text =  viewModel.words[it].spanish
                    }
                    else -> {
                        binding.tvCard.text = "${viewModel.words[it].polish}\n\n${viewModel.words[it].spanish}"
                    }
                }
            }
            //background word
            if (it + 1 < viewModel.words.size) {
                when (viewModel.selectedOption) {
                    0 -> {
                        binding.tvBackground2.text =  viewModel.words[it+1].polish
                    }
                    1 -> {
                        binding.tvBackground2.text =  viewModel.words[it+1].spanish
                    }
                    else -> {
                        binding.tvBackground2.text = "${viewModel.words[it+1].polish}\n\n${viewModel.words[it+1].spanish}"
                    }
                }
            }
            //progressbar
            if (it - 1 < viewModel.words.size) {
                binding.tvProgress.text = "$it / ${viewModel.words.size}"
                binding.progressBar.progress = it
            }
            //finish
            if(it >= viewModel.words.size) {
                sharedViewModel.flashcardCorrectAnswers = viewModel.correctAnswers
                sharedViewModel.flashcardIncorrectAnswers = viewModel.incorrectAnswers
                sharedViewModel.flashcardAnswers = viewModel.answers
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.writeFragment, true)
                    .build()
                findNavController().navigate(
                    FlashcardFragmentDirections.actionFlashcardFragmentToEndFlashcardFragment(),
                    navOptions
                )
            }
        })

        viewModel.knownWords.observe(viewLifecycleOwner, {
            binding.tvWordKnown.text = it.toString()
        })

        viewModel.unknownWords.observe(viewLifecycleOwner, {
            binding.tvWordUnknown.text = it.toString()
        })

    }

}