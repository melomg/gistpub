package com.projects.melih.gistpub.components

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

import java.lang.ref.WeakReference

/**
 * Created by melih on 17/08/2017
 */

class HideSoftKeyboard(v: View?, hideKeyboard: Boolean = true) {
    private lateinit var v: View

    init {
        if (v != null) {
            v.isEnabled = false
            this.v = v
            if (hideKeyboard) {
                val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
            launch {
                delay(time = v.context.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong())
                this@HideSoftKeyboard.v.isEnabled = true
            }
        }
    }
}
