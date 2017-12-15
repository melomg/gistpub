package com.projects.melih.gistpub.liveviewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.projects.melih.gistpub.model.Gist
import android.arch.paging.PagedList
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.projects.melih.gistpub.Constants
import com.projects.melih.gistpub.model.repository.gist.GistModel
import com.projects.melih.gistpub.network.service.ErrorCode
import com.projects.melih.gistpub.network.service.ErrorMessage

/**
 * Created by melih on 14.10.2017
 */
open class YourGistsViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var data: Pair<LiveData<PagedList<Gist>>?, MediatorLiveData<ErrorMessage>?>
    private var allYourGistsLiveData: LiveData<PagedList<Gist>>? = null
    private val userNameLiveData = MutableLiveData<String>()
    val isLoadingLiveData = MediatorLiveData<Boolean>()
    val isEmptyLiveData = MediatorLiveData<Boolean>()
    val errorLiveData = MediatorLiveData<ErrorMessage>()

    val gists: Pair<LiveData<PagedList<Gist>>?, MediatorLiveData<ErrorMessage>?>
        get() {
            if (allYourGistsLiveData == null) {
                //TODO get user name from model not from view
                val gistModel = GistModel(getApplication()).getYourGists(userNameLiveData.value!!)
                if (gistModel.first != null) {
                    allYourGistsLiveData = gistModel.first?.create(0,
                            PagedList.Config.Builder()
                                    .setPageSize(Constants.PAGED_LIST_PAGE_SIZE)
                                    .setInitialLoadSizeHint(Constants.PAGED_LIST_PAGE_SIZE)
                                    .setEnablePlaceholders(Constants.PAGED_LIST_ENABLE_PLACEHOLDERS)
                                    .build())

                    isLoadingLiveData.addSource(allYourGistsLiveData) { isLoadingLiveData.value = false }
                    isEmptyLiveData.addSource(allYourGistsLiveData) {
                        if (it?.size == 0) {
                            isEmptyLiveData.value = true
                        }
                    }
                    errorLiveData.addSource(allYourGistsLiveData) {
                        if (it == null) {
                            errorLiveData.value = ErrorMessage(errorCode = ErrorCode.GENERAL_ERROR)
                        }
                    }
                } else {
                    errorLiveData.value = gistModel.second
                    isLoadingLiveData.value = false
                }
                data = Pair(allYourGistsLiveData, errorLiveData)
            }
            return data
        }

    fun setUserName(userName: String) {
        //TODO enable this to show loading
        isLoadingLiveData.value = true
        userNameLiveData.value = userName
        allYourGistsLiveData = null
        gists
    }
}
