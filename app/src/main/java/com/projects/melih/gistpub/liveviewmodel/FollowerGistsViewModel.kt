package com.projects.melih.gistpub.liveviewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.projects.melih.gistpub.Constants
import com.projects.melih.gistpub.model.Gist
import com.projects.melih.gistpub.model.repository.gist.GistModel
import com.projects.melih.gistpub.network.service.ErrorCode
import com.projects.melih.gistpub.network.service.ErrorMessage

/**
 * Created by melih on 14.10.2017
 */
open class FollowerGistsViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var data: Pair<LiveData<PagedList<Gist>>?, MediatorLiveData<ErrorMessage>?>
    private var allFollowerGistsLiveData: LiveData<PagedList<Gist>>? = null
    private val followerNameLiveData = MutableLiveData<String>()
    val isLoadingLiveData = MediatorLiveData<Boolean>()
    val isEmptyLiveData = MediatorLiveData<Boolean>()
    val errorLiveData = MediatorLiveData<ErrorMessage>()

    val gists: Pair<LiveData<PagedList<Gist>>?, MediatorLiveData<ErrorMessage>?>
        get() {
            if (allFollowerGistsLiveData == null) {
                val gistModel = GistModel(getApplication()).getFollowerGists(followerNameLiveData.value!!)
                if (gistModel.first != null) {
                    allFollowerGistsLiveData = gistModel.first?.create(0,
                            PagedList.Config.Builder()
                                    .setPageSize(Constants.PAGED_LIST_PAGE_SIZE)
                                    .setInitialLoadSizeHint(Constants.PAGED_LIST_PAGE_SIZE)
                                    .setEnablePlaceholders(Constants.PAGED_LIST_ENABLE_PLACEHOLDERS)
                                    .build())

                    isLoadingLiveData.addSource(allFollowerGistsLiveData) { isLoadingLiveData.value = false }
                    isEmptyLiveData.addSource(allFollowerGistsLiveData) {
                        if (it?.size == 0) {
                            isEmptyLiveData.value = true
                        }
                    }
                    errorLiveData.addSource(allFollowerGistsLiveData) {
                        if (it == null) {
                            errorLiveData.value = ErrorMessage(errorCode = ErrorCode.GENERAL_ERROR)
                        }
                    }
                } else {
                    errorLiveData.value = gistModel.second
                    isLoadingLiveData.value = false
                }
                data = Pair(allFollowerGistsLiveData, errorLiveData)

            }
            return data
        }

    fun setFollowerName(followerName: String) {
        //TODO enable this to show loading
        isLoadingLiveData.value = true
        followerNameLiveData.value = followerName
        allFollowerGistsLiveData = null
        gists
    }
}