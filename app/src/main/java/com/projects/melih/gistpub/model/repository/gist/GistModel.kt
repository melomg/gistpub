package com.projects.melih.gistpub.model.repository.gist

import android.arch.paging.LivePagedListProvider
import android.content.Context
import com.projects.melih.gistpub.manager.NetManager
import com.projects.melih.gistpub.model.Gist
import com.projects.melih.gistpub.network.service.GitHubApi

/**
 * Created by melih on 24/09/2017.
 */
open class GistModel(private val netManager: NetManager, private val context: Context) {
    private val remoteDataSource = GistRemoteDataSource()

    fun getYourGists(userName: String): LivePagedListProvider<Int, Gist> {
        return remoteDataSource.getYourGists(userName, GitHubApi.getGitHubService(context))
    }

    fun getStarredGists(): LivePagedListProvider<Int, Gist> {
        return remoteDataSource.getStarredGists(GitHubApi.getGitHubService(context))
    }

    fun getFollowerGists(followerName: String): LivePagedListProvider<Int, Gist> {
        return remoteDataSource.getFollowerGists(followerName, GitHubApi.getGitHubService(context))
    }
}