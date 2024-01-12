package com.cookbook.app.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
//        value = "This is Favorites Fragment which will list all the shortlisted recipes by the end user"
    }
    val text: LiveData<String> = _text
}