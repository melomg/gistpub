package com.projects.melih.gistpub.model.repository.follower

import android.arch.paging.LivePagedListProvider
import android.content.Context
import com.projects.melih.gistpub.manager.NetManager
import com.projects.melih.gistpub.model.Contributor
import com.projects.melih.gistpub.network.service.GitHubApi

/**
 * Created by melih on 15.10.2017.
 */
open class FollowerModel(private val netManager: NetManager, private val context: Context) {
    private val remoteDataSource = FollowerRemoteDataSource()

    fun getYourFollowers(userName: String): LivePagedListProvider<Int, Contributor> {
        return remoteDataSource.getYourFollowers(userName, GitHubApi.getGitHubService(context))
    }
}