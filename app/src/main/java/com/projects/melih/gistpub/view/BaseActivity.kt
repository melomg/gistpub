package com.projects.melih.gistpub.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar


/**
 * Created by melih on 23/12/2016
 */

abstract class BaseActivity : AppCompatActivity(), NavigationListener {

    private lateinit var navigationObserver: NavigationObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationObserver = NavigationObserver(lifecycle, supportFragmentManager, this)
        lifecycle.addObserver(navigationObserver)
    }

    override fun openFragment(newFragment: Fragment, backStack: String?, animType: Int) {
        navigationObserver.openFragment(newFragment, backStack, animType)
    }

    override fun openFragmentAndClearStack(newFragment: Fragment, animType: Int) {
        navigationObserver.openFragmentAndClearStack(newFragment, animType)
    }

    override fun initToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            super.finish()
        }
    }

    protected fun clearBackStack() {
        navigationObserver.clearBackStack()
    }
}


