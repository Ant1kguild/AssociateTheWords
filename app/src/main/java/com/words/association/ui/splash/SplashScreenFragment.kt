package com.words.association.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.words.association.R
import com.words.association.data.datasource.firebase.model.AuthStatus
import com.words.association.databinding.FragmentSplashScreenBinding
import com.words.association.ui.NavigationViewModel
import com.words.association.ui.models.NavigationAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenFragment : Fragment() {

    private val navigationViewModel by sharedViewModel<NavigationViewModel>()
    private val splashScreenViewModel by viewModel<SplashScreenViewModel>()

    private lateinit var binding: FragmentSplashScreenBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_splash_screen,
                container,
                false
            )

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        splashScreenViewModel.authStatus.observe(viewLifecycleOwner) {
            CoroutineScope(Dispatchers.Main).launch {
                delay(500)
                when (it) {
                    is AuthStatus.Authorized -> {
                        navigationViewModel.setUser(it.user)
                        navigationViewModel.setScreen(NavigationAction.OpenVocabulary)
                    }
                    else -> {
                        navigationViewModel.setScreen(NavigationAction.OpenLogin)
                    }
                }
            }
        }
    }


    companion object {
        private const val TAG = "SplashScreenFragment"
    }
}
