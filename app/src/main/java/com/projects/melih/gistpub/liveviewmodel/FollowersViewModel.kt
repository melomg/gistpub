package com.projects.melih.gistpub.liveviewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.projects.melih.gistpub.Constants
import com.projects.melih.gistpub.model.Contributor
import com.projects.melih.gistpub.model.repository.follower.FollowerModel
import com.projects.melih.gistpub.network.service.ErrorCode
import com.projects.melih.gistpub.network.service.ErrorMessage

/**
 * Created by melih on 14.10.2017
 */
open class FollowersViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var data: Pair<LiveData<PagedList<Contributor>>?, MediatorLiveData<ErrorMessage>?>
    private var allFollowersLiveData: LiveData<PagedList<Contributor>>? = null
    private val userNameLiveData = MutableLiveData<String>()
    val isLoadingLiveData = MediatorLiveData<Boolean>()
    val isEmptyLiveData = MediatorLiveData<Boolean>()
    val errorLiveData = MediatorLiveData<ErrorMessage>()

    val followers: Pair<LiveData<PagedList<Contributor>>?, MediatorLiveData<ErrorMessage>?>
        get() {
            if (allFollowersLiveData == null) {
                //TODO get user name from model not from view
                val gistModel = FollowerModel(getApplication()).getYourFollowers(userNameLiveData.value!!)
                if (gistModel.first != null) {
                    allFollowersLiveData = gistModel.first?.create(0,
                            PagedList.Config.Builder()
                                    .setPageSize(Constants.PAGED_LIST_PAGE_SIZE)
                                    .setInitialLoadSizeHint(Constants.PAGED_LIST_PAGE_SIZE)
                                    .setEnablePlaceholders(Constants.PAGED_LIST_ENABLE_PLACEHOLDERS)
                                    .build())

                    isLoadingLiveData.addSource(allFollowersLiveData) { isLoadingLiveData.value = false }
                    isEmptyLiveData.addSource(allFollowersLiveData) {
                        if (it?.size == 0) {
                            isEmptyLiveData.value = true
                        }
                    }
                    errorLiveData.addSource(allFollowersLiveData) {
                        if (it == null) {
                            errorLiveData.value = ErrorMessage(errorCode = ErrorCode.GENERAL_ERROR)
                        }
                    }
                } else {
                    errorLiveData.value = gistModel.second
                    isLoadingLiveData.value = false
                }
                data = Pair(allFollowersLiveData, errorLiveData)
            }
            return data
        }

    fun setUserName(userName: String) {
        isLoadingLiveData.value = true
        userNameLiveData.value = userName
        allFollowersLiveData = null
        followers
    }
}