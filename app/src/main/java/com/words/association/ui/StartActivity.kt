package com.words.association.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.words.association.R
import com.words.association.databinding.ActivityStartBinding
import com.words.association.ui.add_word.AddWordFragmentDirections
import com.words.association.ui.models.NavigationAction
import com.words.association.ui.vocabulary.VocabularyFragmentDirections
import org.koin.androidx.viewmodel.ext.android.viewModel

class StartActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "StartActivity"
    }

    //ViewModel
    private val navigationViewModel by viewModel<NavigationViewModel>()

    //DataBinding
    private lateinit var binding: ActivityStartBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_start)
        navController = findNavController(R.id.nav_host_fragment)
        navigationViewModel.navigationAction.observe(this, Observer(::navigateTo))
    }





    private fun navigateTo(screen: NavigationAction) {
        when (screen) {
            is NavigationAction.OpenSplash -> navController.navigate(R.id.nav_splash_screen)
            is NavigationAction.OpenLogin -> navController.navigate(R.id.nav_login)
            is NavigationAction.OpenVocabulary -> navController.navigate(R.id.nav_vocabulary)
            is NavigationAction.OpenAddWord -> navController.navigate(R.id.nav_add_word)
            is NavigationAction.OpenWordDetailsFromVocabulary -> navController.navigate(
                VocabularyFragmentDirections.actionNavVocabularyToNavWordDetails(
                    screen.word
                )
            )
            is NavigationAction.OpenWordDetailsFromAddWord -> navController.navigate(
                AddWordFragmentDirections.actionNavAddWordToNavWordDetails(
                    screen.word
                ), NavOptions.Builder().setPopUpTo(R.id.nav_add_word, true).build()
            )
        }
    }

    override fun onBackPressed() {
        when (navController.currentDestination!!.id) {
            R.id.nav_vocabulary -> finish()
            R.id.nav_splash_screen -> finish()
            R.id.nav_login -> finish()
            else -> super.onBackPressed()
        }
    }
}





