package com.example.learnspanishwithcrys.ui.match

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.learnspanishwithcrys.R
import com.example.learnspanishwithcrys.databinding.EndMatchFragmentBinding
import com.example.learnspanishwithcrys.other.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EndMatchFragment : Fragment(R.layout.end_match_fragment) {
    private val args: EndMatchFragmentArgs by navArgs()
    private lateinit var binding: EndMatchFragmentBinding
    private val viewModel: EndMatchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EndMatchFragmentBinding.bind(view)

        if (args.victory) {
            subscribeToObservers()
            val time = args.time.toDouble()
            binding.tv.text = (context?.getString(R.string.won) + " " + time)
            binding.tv.setTextColor(Color.GREEN)
            viewModel.addResult(time)
            viewModel.allResults()
        } else {
            binding.tv.text = context?.getString(R.string.lose)
            binding.tv.setTextColor(Color.RED)
        }

        binding.btnRestart.setOnClickListener {
            findNavController().navigate(
                EndMatchFragmentDirections.actionEndMatchFragmentToMatchFragment()
            )
        }

        binding.btnBackToMenu.setOnClickListener {
            findNavController().navigate(
                EndMatchFragmentDirections.actionEndMatchFragmentToMenuFragment()
            )
        }

    }

    private fun subscribeToObservers() {
        viewModel.resultStatus.observe(viewLifecycleOwner, { result ->
            when(result.status) {
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    val text = SpannableStringBuilder()
                    for (i in result.data!!.indices) {
                        var textLine = "${i + 1}. " + result.data[i].user + " " + result.data[i].time + "\n"
                        if (i < 9) {
                            textLine = "0$textLine"
                        }
                        val span = SpannableString(textLine)
                        if (result.data[i].user == viewModel.user && result.data[i].time == args.time.toDouble()) {
                            span.setSpan(ForegroundColorSpan(Color.GREEN), 0, span.length, 0)
                        }
                        text.append(span)
                    }
                    binding.tvResults.text = text
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    val message = result.message ?: "unknown error"
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }
}