package com.words.association.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.words.association.data.datasource.firebase.model.User
import com.words.association.ui.models.NavigationAction


class NavigationViewModel : ViewModel() {

    companion object {
        private const val TAG = "NavigationViewModel"
    }

    //LiveData --> user
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    //LiveData --> screen
    private val mutableNavigationAction: MutableLiveData<NavigationAction> = MutableLiveData()
    val navigationAction: LiveData<NavigationAction> = mutableNavigationAction

    init {
        mutableNavigationAction.value = NavigationAction.OpenSplash
    }

    fun setUser(user: User) {
        _user.value = user
    }

    fun setScreen(screen: NavigationAction) {
        mutableNavigationAction.value = screen
    }
}