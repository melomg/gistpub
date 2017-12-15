package com.projects.melih.gistpub.liveviewmodel

import android.app.Application
import android.arch.lifecycle.*
import android.arch.paging.PagedList
import com.projects.melih.gistpub.Constants
import com.projects.melih.gistpub.model.Gist
import com.projects.melih.gistpub.model.repository.gist.GistModel
import com.projects.melih.gistpub.network.service.ErrorCode
import com.projects.melih.gistpub.network.service.ErrorMessage

/**
 * Created by melih on 14.10.2017
 */
open class StarredGistsViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var data: Pair<LiveData<PagedList<Gist>>?, MediatorLiveData<ErrorMessage>?>
    private var allStarredGistsLiveData: LiveData<PagedList<Gist>>? = null
    val isLoadingLiveData = MediatorLiveData<Boolean>()
    val isEmptyLiveData = MediatorLiveData<Boolean>()
    val errorLiveData = MediatorLiveData<ErrorMessage>()

    val gists: Pair<LiveData<PagedList<Gist>>?, MediatorLiveData<ErrorMessage>?>
        get() {
            if (allStarredGistsLiveData == null) {
                val gistModel = GistModel(getApplication()).getStarredGists()
                if (gistModel.first != null) {
                    allStarredGistsLiveData = gistModel.first?.create(0,
                            PagedList.Config.Builder()
                                    .setPageSize(Constants.PAGED_LIST_PAGE_SIZE)
                                    .setInitialLoadSizeHint(Constants.PAGED_LIST_PAGE_SIZE)
                                    .setEnablePlaceholders(Constants.PAGED_LIST_ENABLE_PLACEHOLDERS)
                                    .build())

                    isLoadingLiveData.addSource(allStarredGistsLiveData) { isLoadingLiveData.value = false }
                    isEmptyLiveData.addSource(allStarredGistsLiveData) {
                        if (it?.size == 0) {
                            isEmptyLiveData.value = true
                        }
                    }
                    errorLiveData.addSource(allStarredGistsLiveData) {
                        if (it == null) {
                            errorLiveData.value = ErrorMessage(errorCode = ErrorCode.GENERAL_ERROR)
                        }
                    }
                } else {
                    errorLiveData.value = gistModel.second
                    isLoadingLiveData.value = false
                }
                data = Pair(allStarredGistsLiveData, errorLiveData)
            }
            return data
        }

    fun refresh() {
        //TODO enable this to show loading
        //isLoadingLiveData.value = true
        allStarredGistsLiveData = null
        gists
    }
}