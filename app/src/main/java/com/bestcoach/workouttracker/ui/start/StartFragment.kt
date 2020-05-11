package com.bestcoach.workouttracker.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.bestcoach.workouttracker.R
import com.bestcoach.workouttracker.databinding.FragmentStartBinding

class StartFragment : Fragment() {
    private lateinit var binding: FragmentStartBinding
    private val viewModel by viewModels<StartViewModel> { defaultViewModelProviderFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        binding.lifecycleOwner = this.viewLifecycleOwner

        // access the items of the list
        val exercises = resources.getStringArray(R.array.Exercises)

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item, exercises
        )
        binding.workoutSpinner.adapter = adapter

        viewModel.eventNavigateToCamera.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(
                    StartFragmentDirections.actionStartFragmentToCameraFragment(
                        binding.workoutSpinner.selectedItem.toString()
                    )
                )
                viewModel.doneNavigatingToCamera()
            }
        }
        return binding.root
    }
}
