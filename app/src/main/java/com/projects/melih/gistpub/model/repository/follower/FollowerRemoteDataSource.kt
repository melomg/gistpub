package com.projects.melih.gistpub.model.repository.follower

import android.arch.paging.DataSource
import android.arch.paging.LivePagedListProvider
import android.arch.paging.TiledDataSource
import com.projects.melih.gistpub.model.Contributor
import com.projects.melih.gistpub.network.service.GitHubService
import retrofit2.Response
import java.util.ArrayList

/**
 * Created by melih on 24/09/2017
 */
open class FollowerRemoteDataSource {

    fun getYourFollowers(username: String, dataProvider: GitHubService): LivePagedListProvider<Int, Contributor> {
        return object : LivePagedListProvider<Int, Contributor>() {
            override fun createDataSource(): FollowerTiledRemoteDataSource<Contributor> = object : FollowerTiledRemoteDataSource<Contributor>(username, dataProvider) {
                override fun convertToItems(items: ArrayList<Contributor>?): ArrayList<Contributor> {
                    return items!!
                }
            }
        }
    }
}

abstract class FollowerTiledRemoteDataSource<T> protected constructor(val owner: String, val dataProvider: GitHubService) : TiledDataSource<T>() {
    protected abstract fun convertToItems(items: ArrayList<Contributor>?): ArrayList<T>?

    override fun countItems(): Int = DataSource.COUNT_UNDEFINED

    override fun loadRange(startPosition: Int, loadCount: Int): List<T>? {
        val response: Response<ArrayList<Contributor>>? = dataProvider.getYourFollowers(owner, startPosition.toString(), loadCount.toString()).execute()
        return convertToItems(response?.body())
    }
}