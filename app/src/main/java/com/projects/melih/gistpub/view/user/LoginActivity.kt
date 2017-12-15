package com.projects.melih.gistpub.view.user

import android.os.Bundle

import com.projects.melih.gistpub.R
import com.projects.melih.gistpub.view.BaseActivity
import com.projects.melih.gistpub.view.NavigationObserver

open class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        openFragmentAndClearStack(BasicAuthFragment.newInstance(), NavigationObserver.Companion.NONE)
    }
}
