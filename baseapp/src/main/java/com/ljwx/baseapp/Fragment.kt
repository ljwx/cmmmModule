package com.ljwx.baseapp

import android.os.Bundle
import android.view.View
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ljwx.baseapp.vm.EmptyViewModel

class Fragment : androidx.fragment.app.Fragment() {

    val factory = viewModelFactory {
        initializer {
            EmptyViewModel()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }
}