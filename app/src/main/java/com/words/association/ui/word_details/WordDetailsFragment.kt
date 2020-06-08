package com.words.association.ui.word_details

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.github.zagum.switchicon.SwitchIconView
import com.jakewharton.rxbinding3.view.clicks
import com.labo.kaji.relativepopupwindow.RelativePopupWindow
import com.roger.catloadinglibrary.CatLoadingView
import com.words.association.R
import com.words.association.databinding.FragmentWordDetailsBinding
import com.words.association.ui.NavigationViewModel
import io.reactivex.subjects.PublishSubject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class WordDetailsFragment : Fragment(), WordsDetailsViewModel.View {


    private lateinit var binding: FragmentWordDetailsBinding
    private var loadingView: CatLoadingView? = null
    private var popupWindowTranslate: WordTranslatePopupWindow? = null
    private var popupWordDefinition: WordDefinitionsPopupWindow? = null

    private val sharedViewModel by sharedViewModel<NavigationViewModel>()
    private val wordsDetailsViewModel by viewModel<WordsDetailsViewModel> { parametersOf(this) }

    private val args: WordDetailsFragmentArgs by navArgs()
    private val clickAssociationSubject = PublishSubject.create<String>()
    private val addAssociationSubject = PublishSubject.create<String>()

    private val wordDetailslAdapter: WordDetailsAssociationAdapter by lazy {
        WordDetailsAssociationAdapter({
            clickAssociationSubject.onNext(it)
        }, {
            addAssociationSubject.onNext(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate(inflater, R.layout.fragment_word_details, container, false)
        val chipsLayoutManager =
            ChipsLayoutManager.newBuilder(activity)
                .setChildGravity(Gravity.CENTER)
                .setScrollingEnabled(true)
                .setMaxViewsInRow(4)
                .setGravityResolver { Gravity.CENTER }
                .setRowBreaker { false }
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_CENTER_DENSE)
                .withLastRow(true)
                .build()

        bindClickListeners()

        binding.rvWordDetails.apply {
            adapter = wordDetailslAdapter
            layoutManager = chipsLayoutManager
        }

        binding.tvCurrentWord.text = args.word

        wordsDetailsViewModel.pronunciationLiveData.observe(viewLifecycleOwner) { viewModel ->
            viewModel.onSuccess {
                binding.tvWordPronunciation.text = it
            }
        }

        wordsDetailsViewModel.association.observe(viewLifecycleOwner) { viewModel ->
            viewModel
                .onLoading { showProgress() }
                .onSuccess {
                    wordDetailslAdapter.setData(it)
                    hideProgress()
                }
                .onFailed { hideProgress() }
        }

        wordsDetailsViewModel.addAssociationProgress.observe(viewLifecycleOwner) { viewModel ->
            viewModel
                .onLoading { showProgress() }
                .onSuccess {
                    hideProgress()
                }
                .onFailed { hideProgress() }
        }

        wordsDetailsViewModel.backButton.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }

        wordsDetailsViewModel.toggleTranslate.observe(viewLifecycleOwner) {
            binding.btnTranslate.apply {
                val popupWindow =
                    popupWindowTranslate ?: WordTranslatePopupWindow(context).apply {
                        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        setOnDismissListener {
                            wordsDetailsViewModel.dismissWindow()
                        }
                    }

                popupWindowTranslate = popupWindow
                popupWindowTranslate?.showPopupWindow(it, this)

                this.switchIconState(enable = it)
            }
        }

        wordsDetailsViewModel.toggleDefinition.observe(viewLifecycleOwner) {
            binding.btnDefinition.apply {
                val popupWindow =
                    popupWordDefinition ?: WordDefinitionsPopupWindow(context).apply {
                        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        setOnDismissListener {
                            wordsDetailsViewModel.dismissWindow()
                        }
                    }
                popupWordDefinition = popupWindow
                popupWordDefinition?.showPopupWindow(it, this)

                this.switchIconState(enable = it)
            }
        }

        wordsDetailsViewModel.translationLiveData.observe(viewLifecycleOwner) { viewState ->
            viewState
                .onSuccess { popupWindowTranslate?.setData(it) }
                .onLoading { popupWindowTranslate?.setLoading() }
        }

        wordsDetailsViewModel.definitionLiveData.observe(viewLifecycleOwner) { viewState ->
            viewState
                .onSuccess { popupWordDefinition?.setData(it) }
                .onLoading { popupWordDefinition?.setLoading() }

        }

        return binding.root
    }

    private fun bindClickListeners() {
        binding.btnTranslate.setOnClickListener {
            wordsDetailsViewModel.toggleTranslate(enable = !binding.btnTranslate.isIconEnabled)
        }

        binding.btnDefinition.setOnClickListener {
            wordsDetailsViewModel.toggleDefinition(enable = !binding.btnDefinition.isIconEnabled)
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
        private const val TAG = "WordDetailsFragment"
    }


    private fun RelativePopupWindow.showNearView(
        view: View,
        verticalPosition: Int = RelativePopupWindow.VerticalPosition.ABOVE,
        horizontalPosition: Int = RelativePopupWindow.HorizontalPosition.ALIGN_LEFT
    ) {
        showOnAnchor(view, verticalPosition, horizontalPosition)
    }

    private fun RelativePopupWindow.showPopupWindow(enable: Boolean, view: View) {
        if (enable) {
            showNearView(view)
        } else {
            dismiss()
        }
    }

    private fun SwitchIconView.switchIconState(
        enable: Boolean,
        animate: Boolean = true,
        resIdPressed: Int = R.drawable.btn_circle_presed,
        resIdDisabled: Int = R.drawable.btn_circle_disabled
    ) {
        setIconEnabled(enable, animate)
        background = getDrawable(
            context, if (enable) {
                resIdPressed
            } else {
                resIdDisabled
            }
        )
    }

    override fun getWord() = args.word

    override fun onClickAssociation() = clickAssociationSubject

    override fun onAddAssociation() = addAssociationSubject

    override fun onBackClick() = binding.btnBack.clicks()

}