package com.projects.melih.gistpub.model.repository.gist

import android.os.Handler
import com.projects.melih.gistpub.model.Gist

/**
 * Created by melih on 24/09/2017
 */
open class GistLocalDataSource {
    fun getRepositories(onRepositoryReadyCallback: OnRepoLocalReadyCallback) {
        var arrayList = ArrayList<Gist>()
        /*arrayList.add(Gist("First From Local", "Owner 1", 100, false))
        arrayList.add(Gist("Second From Local", "Owner 2", 30, true))
        arrayList.add(Gist("Third From Local", "Owner 3", 430, false))*/

        Handler().postDelayed({ onRepositoryReadyCallback.onLocalDataReady(arrayList) }, 2000)
    }

    fun saveRepositories(arrayList: ArrayList<Gist>) {
        //todo save repositories in DB
    }
}

interface OnRepoLocalReadyCallback {
    fun onLocalDataReady(data: ArrayList<Gist>)
}