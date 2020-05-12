package com.bestcoach.workouttracker.ui.splash

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bestcoach.workouttracker.R
import com.bestcoach.workouttracker.databinding.FragmentSplashBinding

private const val SPLASH_TIME_OUT = 3000L

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSplashBinding.inflate(inflater, container, false)

        binding.logoImage.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.translate
            )
        )

        Handler().postDelayed({
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToPermissionsFragment())

        }, SPLASH_TIME_OUT)

        return binding.root
    }

}
