package com.projects.melih.gistpub.model.repository.follower

import android.arch.paging.LivePagedListProvider
import android.content.Context
import com.projects.melih.gistpub.manager.NetManager
import com.projects.melih.gistpub.model.Contributor
import com.projects.melih.gistpub.network.service.ErrorCode
import com.projects.melih.gistpub.network.service.ErrorMessage
import com.projects.melih.gistpub.network.service.GitHubApi

/**
 * Created by melih on 15.10.2017.
 */
open class FollowerModel(private val context: Context) {
    private val remoteDataSource = FollowerRemoteDataSource()
    private val netManager = NetManager(context.applicationContext)

    fun getYourFollowers(userName: String): Pair<LivePagedListProvider<Int, Contributor>?, ErrorMessage?> {
        return if (netManager.isConnectedToInternet) {
            Pair(remoteDataSource.getYourFollowers(userName, GitHubApi.getGitHubService(context)), null)
        } else {
            Pair(null, ErrorMessage(ErrorCode.NO_NETWORK))
        }
    }
}