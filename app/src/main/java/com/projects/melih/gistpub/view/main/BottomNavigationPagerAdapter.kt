package com.projects.melih.gistpub.view.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.projects.melih.gistpub.view.gist.follower.FollowersFragment
import com.projects.melih.gistpub.view.gist.starredGists.StarredGistsFragment
import com.projects.melih.gistpub.view.gist.yourGists.YourGistsFragment

/**
 * Created by melih on 13/08/2017
 */

open class BottomNavigationPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            YOUR_GISTS -> {
                YourGistsFragment.newInstance()
            }
            STARRED_GISTS -> {
                StarredGistsFragment.newInstance()
            }
            FOLLOWINGS -> {
                FollowersFragment.newInstance()
            }
            else -> {
                YourGistsFragment.newInstance()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return null
    }

    override fun getCount(): Int {
        return 3
    }

    companion object {
        val YOUR_GISTS = 0
        val STARRED_GISTS = YOUR_GISTS + 1
        val FOLLOWINGS = STARRED_GISTS + 1
    }
}