package com.projects.melih.gistpub.view.main

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.projects.melih.gistpub.Constants
import com.projects.melih.gistpub.R
import com.projects.melih.gistpub.view.BaseFragment
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.util.Log
import com.projects.melih.gistpub.R.id.viewPager


open class HomeFragment : BaseFragment() {
    private lateinit var viewPager: ViewPager
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var listener: ViewPager.OnPageChangeListener

    override val menuRes: Int
        get() = Constants.NO_RES

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater?.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = view?.findViewById<ViewPager>(R.id.viewPager) as ViewPager
        bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottom_navigation) as BottomNavigationView
        viewPager.adapter = BottomNavigationPagerAdapter(childFragmentManager)

        listener = object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                bottomNavigationView.selectedItemId = when (position) {
                    BottomNavigationPagerAdapter.YOUR_GISTS -> {
                        R.id.action_your_gists
                    }
                    BottomNavigationPagerAdapter.STARRED_GISTS -> {
                        R.id.action_starred_gists
                    }
                    BottomNavigationPagerAdapter.FOLLOWINGS -> {
                        R.id.action_followers
                    }
                    else -> R.id.action_your_gists
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        }

        viewPager.addOnPageChangeListener(listener)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            viewPager.currentItem = when (item.itemId) {
                R.id.action_your_gists -> {
                    BottomNavigationPagerAdapter.YOUR_GISTS
                }
                R.id.action_starred_gists -> {
                    BottomNavigationPagerAdapter.STARRED_GISTS
                }
                R.id.action_followers -> {
                    BottomNavigationPagerAdapter.FOLLOWINGS
                }
                else -> {
                    BottomNavigationPagerAdapter.YOUR_GISTS
                }
            }
            true
        }
    }

    override fun onStop() {
        super.onStop()
        viewPager.removeOnPageChangeListener(listener)
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}
