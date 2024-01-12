package com.cookbook.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Recipes List Fragment which will contain list view of all recipes"
    }
    val text: LiveData<String> = _text
}