package com.words.association.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding3.view.clicks
import com.words.association.R
import com.words.association.data.datasource.firebase.model.AuthStatus
import com.words.association.databinding.FragmentLoginBinding
import com.words.association.ui.NavigationViewModel
import com.words.association.ui.models.NavigationAction
import io.reactivex.subjects.PublishSubject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LoginFragment : Fragment(), LoginViewModel.View {

    private val navigationViewModel by sharedViewModel<NavigationViewModel>()
    private val loginViewModel by viewModel<LoginViewModel> {
        parametersOf(this)
    }

    private lateinit var binding: FragmentLoginBinding

    private val authResultSubject = PublishSubject.create<Intent>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        loginViewModel.authIntentLiveObservable.observe(viewLifecycleOwner) {
            startActivityForResult(it.getIntent(), RC_SIGN_IN)
        }
        loginViewModel.authResult.observe(viewLifecycleOwner) {
            when (it) {
                is AuthStatus.Authorized -> navigationViewModel.setScreen(NavigationAction.OpenVocabulary)
                else -> Toast.makeText(context, "Not authorized.", LENGTH_LONG).show()
            }
        }
        return binding.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN && data != null) {
            authResultSubject.onNext(data)
        }
    }


    companion object {
        const val RC_SIGN_IN = 2802
        private const val TAG = "LoginFragment"
    }

    override fun onClickSignIn() = binding.btnGoogleSigIn.clicks()
    override fun onAuthResult() = authResultSubject
}


