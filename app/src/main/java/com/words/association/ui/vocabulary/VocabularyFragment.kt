package com.words.association.ui.vocabulary

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.daasuu.bl.ArrowDirection
import com.jakewharton.rxbinding3.view.clicks
import com.words.association.R
import com.words.association.data.datasource.firebase.model.WordAssociation
import com.words.association.databinding.FragmentMyVocabularyBinding
import com.words.association.databinding.TutorialInfoNextCancelBinding
import com.words.association.databinding.TutorialInfoOkBinding
import com.words.association.ui.NavigationViewModel
import com.words.association.ui.models.NavigationAction
import com.words.association.ui.popup_window.TutorialPopupWindow
import com.words.association.utils.lighter.LighterHelper
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import me.samlss.lighter.Lighter
import me.samlss.lighter.parameter.Direction
import me.samlss.lighter.parameter.LighterParameter
import me.samlss.lighter.shape.RectShape
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class VocabularyFragment : Fragment(), VocabularyViewModel.View {

    //viewModels
    private val vocabularyViewModel: VocabularyViewModel by viewModel {
        parametersOf(this, findNavController())
    }
    private val navigationViewModel: NavigationViewModel by sharedViewModel()

    private var tutorialPopupWindow: TutorialPopupWindow? = null
    private var tutorialLighter: Lighter? = null


    private lateinit var binding: FragmentMyVocabularyBinding
    private lateinit var tutorialInfoBinding: TutorialInfoNextCancelBinding
    private lateinit var tutorialBindingVocItem: TutorialInfoOkBinding
    private lateinit var tutorialBindingVocBtn: TutorialInfoOkBinding
    private val vocabularyAdapter: VocabularyAdapter by lazy {
        VocabularyAdapter {
            clickVocabularyItemSubject.onNext(it)
        }
    }

    private val clickVocabularyItemSubject = PublishSubject.create<WordAssociation>()
    private val onViewCreatedSubject = BehaviorSubject.create<Unit>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_vocabulary, container, false)
        onViewCreatedSubject.onNext(Unit)
        binding.rvVocabulary.adapter = vocabularyAdapter

        tutorialInfoBinding =
            TutorialInfoNextCancelBinding.inflate(inflater, container, false)
        tutorialBindingVocItem =
            TutorialInfoOkBinding.inflate(inflater, container, false)
        tutorialBindingVocBtn =
            TutorialInfoOkBinding.inflate(inflater, container, false)


        observerStandardLiveData()

        vocabularyViewModel.btnStandardClick.observe(viewLifecycleOwner) {
            it
                .onLearnNewWordButtonClick { navigationViewModel.setScreen(NavigationAction.OpenAddWord) }
                .onVocabularyItemClick { word ->
                    navigationViewModel.setScreen(
                        NavigationAction.OpenWordDetailsFromVocabulary(
                            word.key
                        )
                    )
                }
        }

        vocabularyViewModel.btnTutorialClick.observe(viewLifecycleOwner) {
            it
                .onNextTutorial { Log.i("ClickEvent", "$it") }
                .onCloseTutorial { Log.i("ClickEvent", "$it") }
        }


        vocabularyViewModel.tutorialStep.observe(viewLifecycleOwner) {
            hideLighter()
            it
                .onExplanationVocabulary { showLighter(listOf(lightVocabulary())) }
                .onExplanationVocabularyItem { showLighter(listOf(lightVocabularyItem())) }
                .onExplanationBtnLearnNewWord { showLighter(listOf(lighterNewWordBtn())) }
                .onActionPressNavigationBtn {
                    showLighter(
                        listOf(
                            lighterVocItemClick(),
                            lighterButtonClick()
                        )
                    )
                }
                .onCloseStep { }
        }

        return binding.root
    }


    private fun observerStandardLiveData() {
        vocabularyViewModel.apply {
            vocabulary.observe(viewLifecycleOwner) { state ->
                state
                    .onLoading {}
                    .onFailed {}
                    .onSuccess {
                        vocabularyAdapter.setData(it)
                    }
            }
        }
    }

    companion object {
        private const val TAG = "VocabularyFragment"
    }


    private fun showLighter(parameter: List<LighterParameter>) {
        val lighter = tutorialLighter ?: Lighter
            .with(binding.layoutRoot)
            .setIntercept(true)
            .setBackgroundColor(Color.parseColor("#52000000"))
            .addHighlight(*parameter.toTypedArray())
        tutorialLighter = lighter
        tutorialLighter?.show()
    }


    private fun hideLighter() {
        tutorialLighter?.dismiss()
        tutorialLighter = null
    }


    private fun lightVocabulary(): LighterParameter {
        binding.rvVocabulary.apply {
            tutorialInfoBinding.bubbleContainer.arrowDirection =
                ArrowDirection.BOTTOM_CENTER
            tutorialInfoBinding.textInfo.text =
                context.getString(R.string.lighter_vocabulary_text_info)
            val recycleViewShape = RectShape(25.0f, 25.0f, 45.0f)

            return LighterHelper.getLighterParameterView(
                lighterView = this,
                tipLayout = tutorialInfoBinding.root,
                lighterShape = recycleViewShape,
                shapeXOffset = 10.0f,
                shapeYOffset = 10.0f,
                viewDirection = Direction.TOP
            )
        }
    }

    private fun lightVocabularyItem(): LighterParameter {
        binding.rvVocabulary.apply {
            tutorialInfoBinding.notifyChange()
            val view = this.getChildAt(0)
            tutorialInfoBinding.textInfo.text =
                context.getString(R.string.light_vocabulary_item_text_info)
            tutorialInfoBinding.bubbleContainer.arrowDirection =
                ArrowDirection.BOTTOM_CENTER
            val recycleViewShape = RectShape(15.0f, 15.0f, 35.0f)

            return LighterHelper.getLighterParameterView(
                lighterView = view,
                tipLayout = tutorialInfoBinding.root,
                lighterShape = recycleViewShape,
                shapeXOffset = 1.0f,
                shapeYOffset = 1.0f,
                viewDirection = Direction.TOP
            )
        }
    }

    private fun lighterNewWordBtn(): LighterParameter {
        binding.btnLearnNewWord.apply {
            tutorialInfoBinding.apply {
                textInfo.text = context.getString(R.string.lighter_new_word_btn_text_info)
                bubbleContainer.arrowDirection = ArrowDirection.BOTTOM_CENTER
            }

            val recycleViewShape = RectShape(60.0f, 60.0f, 60.0f)

            return LighterHelper.getLighterParameterView(
                lighterView = this,
                tipLayout = tutorialInfoBinding.root,
                lighterShape = recycleViewShape,
                shapeXOffset = 15.0f,
                shapeYOffset = 15.0f,
                viewDirection = Direction.TOP
            )
        }
    }


    private fun lighterVocItemClick(): LighterParameter {
        binding.rvVocabulary.apply {
            val view = this.getChildAt(0)

            tutorialBindingVocItem.textInfo.text =
                context.getString(R.string.light_vocabulary_item_click_text_info)
            tutorialBindingVocItem.bubbleContainer.arrowDirection = ArrowDirection.TOP_CENTER

            val recycleViewShape = RectShape(15.0f, 15.0f, 35.0f)

            return LighterHelper.getLighterParameterView(
                lighterView = view,
                tipLayout = tutorialBindingVocItem.root,
                lighterShape = recycleViewShape,
                shapeXOffset = 1.0f,
                shapeYOffset = 1.0f,
                viewDirection = Direction.BOTTOM
            )
        }
    }

    private fun lighterButtonClick(): LighterParameter {
        binding.btnLearnNewWord.apply {
            tutorialBindingVocBtn.textInfo.text =
                context.getString(R.string.lighter_new_word_btn_click_text_info)
            tutorialBindingVocBtn.bubbleContainer.arrowDirection = ArrowDirection.BOTTOM_CENTER

            val recycleViewShape = RectShape(15.0f, 15.0f, 35.0f)

            return LighterHelper.getLighterParameterView(
                lighterView = this,
                tipLayout = tutorialBindingVocBtn.root,
                lighterShape = recycleViewShape,
                shapeXOffset = 1.0f,
                shapeYOffset = 1.0f,
                viewDirection = Direction.TOP
            )
        }
    }

    override fun onClickVocabularyItem(): Observable<WordAssociation> = clickVocabularyItemSubject
    override fun onClickBtnLearnNewWord(): Observable<Unit> =
        onViewCreatedSubject.switchMap { binding.btnLearnNewWord.clicks() }

    override fun onClickNextStepTutorial(): Observable<Unit> =
        onViewCreatedSubject.switchMap { tutorialInfoBinding.btnNext.clicks() }

    override fun onClickCloseTutorial(): Observable<Unit> = onViewCreatedSubject.switchMap {
        Observable.merge(
            tutorialInfoBinding.btnCancel.clicks(),
            tutorialBindingVocItem.btnOk.clicks(),
            tutorialBindingVocBtn.btnOk.clicks()
        )
    }
}

