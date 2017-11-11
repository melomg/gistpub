package com.projects.melih.gistpub.model.repository.gist

import android.arch.paging.LivePagedListProvider
import android.content.Context
import com.projects.melih.gistpub.manager.NetManager
import com.projects.melih.gistpub.model.Gist
import com.projects.melih.gistpub.network.service.ErrorCode
import com.projects.melih.gistpub.network.service.ErrorMessage
import com.projects.melih.gistpub.network.service.GitHubApi

/**
 * Created by melih on 24/09/2017.
 */
open class GistModel(private val context: Context) {
    private val remoteDataSource = GistRemoteDataSource()
    private val netManager = NetManager(context.applicationContext)

    fun getYourGists(userName: String): Pair<LivePagedListProvider<Int, Gist>?, ErrorMessage?> {
        return if (netManager.isConnectedToInternet) {
            Pair(remoteDataSource.getYourGists(userName, GitHubApi.getGitHubService(context)), null)
        } else {
            Pair(null, ErrorMessage(ErrorCode.NO_NETWORK))
        }
    }

    fun getStarredGists(): Pair<LivePagedListProvider<Int, Gist>?, ErrorMessage?> {
        return if (netManager.isConnectedToInternet) {
            Pair(remoteDataSource.getStarredGists(GitHubApi.getGitHubService(context)), null)
        } else {
            Pair(null, ErrorMessage(ErrorCode.NO_NETWORK))
        }
    }

    fun getFollowerGists(followerName: String): Pair<LivePagedListProvider<Int, Gist>?, ErrorMessage?> {
        return if (netManager.isConnectedToInternet) {
            Pair(remoteDataSource.getFollowerGists(followerName, GitHubApi.getGitHubService(context)), null)
        } else {
            Pair(null, ErrorMessage(ErrorCode.NO_NETWORK))
        }
    }
}