package com.projects.melih.gistpub.view

import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar

/**
 * Created by melih on 16/07/2017
 */

interface NavigationListener {

    fun onBackPressed()

    fun initToolbar(toolbar: Toolbar)

    fun openFragment(newFragment: Fragment, backStack: String?, @NavigationObserver.Companion.SlideAnimType animType: Int = NavigationObserver.LEFT_TO_RIGHT)

    fun openFragmentAndClearStack(newFragment: Fragment, @NavigationObserver.Companion.SlideAnimType animType: Int)
}
