package com.example.app.ui.menu

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class MenuViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is menu Fragment"
    }
    val text: LiveData<String> = _text
}