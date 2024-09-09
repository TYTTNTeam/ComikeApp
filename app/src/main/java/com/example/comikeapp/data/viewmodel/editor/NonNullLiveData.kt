package com.example.comikeapp.data.viewmodel.editor

import androidx.lifecycle.MutableLiveData

class NonNullLiveData<T: Any>(defaultValue: T) : MutableLiveData<T>(defaultValue) {

    init {
        value = defaultValue
    }

    override fun getValue() = super.getValue()!!
}