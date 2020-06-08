package com.words.association.ui.add_word

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.jakewharton.rxbinding3.view.clicks
import com.roger.catloadinglibrary.CatLoadingView
import com.words.association.R
import com.words.association.databinding.FragmentAddWordBinding
import com.words.association.ui.NavigationViewModel
import com.words.association.ui.models.NavigationAction
import io.reactivex.subjects.PublishSubject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class AddWordFragment : Fragment(), AddWordViewModel.View {

    private lateinit var binding: FragmentAddWordBinding
    private val addWordViewModel: AddWordViewModel by viewModel { parametersOf(this) }
    private val navigationViewModel: NavigationViewModel by sharedViewModel()
    private var loadingView: CatLoadingView? = null

    private val adapter: AddWordAdapter by lazy {
        AddWordAdapter {

        }
    }

    private val clickFindSubject = PublishSubject.create<String>()
    private val addWordSubject = PublishSubject.create<Unit>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_word,
            container,
            false
        )

        binding.recyclerView.adapter = adapter

        addWordViewModel.addWord.observe(viewLifecycleOwner) {
            it.onLoading {
                showProgress()
            }.onSuccess { word ->
                navigationViewModel.setScreen(
                    NavigationAction.OpenWordDetailsFromAddWord(word)
                )
                hideProgress()
            }.onFailed {
                hideProgress()
            }
        }

        addWordViewModel.examplesLiveData.observe(viewLifecycleOwner) {
            it.onLoading {
                showProgress()
                setInfoVisible(false)
                binding.tfb.error = null
            }.onSuccess { examples ->
                hideProgress()
                setInfoVisible(true)
                adapter.setData(examples)
                binding.tfb.error = null
            }.onFailed {
                binding.tfb.error = "Word could not be found"
                hideProgress()
                adapter.setData(emptyList())
                setInfoVisible(false)
            }
        }


        addWordViewModel.translationLiveData.observe(viewLifecycleOwner) {
            it.onSuccess { translations ->
                binding.apply {
                    tvWordTranslate.text = translations
                }
            }
        }

        addWordViewModel.pronunciationLiveData.observe(viewLifecycleOwner) {
            it.onSuccess { pronunciation ->
                binding.apply {
                    tvWordPronunciation.text = pronunciation
                }
            }
        }

        addWordViewModel.alreadyExistLiveData.observe(viewLifecycleOwner) {
            it.onSuccess { alreadyExist ->
                binding.ivClickAddWord.isVisible = !alreadyExist
            }
        }

        addWordViewModel.backButton.observe(viewLifecycleOwner) {

            activity?.apply {
                val im = getSystemService<InputMethodManager>()
                im?.hideSoftInputFromWindow(binding.etInputText.windowToken, 0)
                onBackPressed()
            }
        }


        binding.ivClickAddWord.setOnClickListener {
            addWordSubject.onNext(Unit)
        }

        binding.etInputText
            .apply {
                setOnKeyListener { _, keyCode, keyEvent ->
                    if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        clickFindSubject.onNext(binding.etInputText.text.toString())
                        true
                    } else {
                        false
                    }
                }
            }


        return binding.root
    }

    private fun setInfoVisible(isVisible: Boolean) {
        binding.apply {
            tvWordPronunciation.isVisible = isVisible
            tvWordTranslate.isVisible = isVisible
            ivClickAddWord.isVisible = isVisible
        }
    }

    private fun showProgress() {
        val target = activity ?: return
        val progress = loadingView ?: CatLoadingView().apply {
            setCanceledOnTouchOutside(false)
            show(target.supportFragmentManager, "")
        }
        loadingView = progress
    }

    private fun hideProgress() {
        loadingView?.dismiss()
        loadingView = null
    }

    companion object {
        private const val TAG = "AddWordFragment"
    }

    override fun onClickFind() = clickFindSubject

    override fun onClickAdd() = addWordSubject

    override fun onBackClick() = binding.btnBack.clicks()
}
