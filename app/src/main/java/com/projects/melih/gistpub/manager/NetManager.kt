package com.projects.melih.gistpub.manager

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo



/**
 * Created by melih on 24/09/2017.
 */
open class NetManager(private var applicationContext: Context) {
    val isConnectedToInternet: Boolean
        get() {
            val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }
}