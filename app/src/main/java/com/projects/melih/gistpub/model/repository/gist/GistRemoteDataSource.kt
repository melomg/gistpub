package com.projects.melih.gistpub.model.repository.gist

import android.arch.paging.DataSource
import android.arch.paging.LivePagedListProvider
import android.arch.paging.TiledDataSource
import com.projects.melih.gistpub.Constants
import com.projects.melih.gistpub.model.Gist
import com.projects.melih.gistpub.network.service.GitHubService
import retrofit2.Response

/**
 * Created by melih on 24/09/2017
 */
open class GistRemoteDataSource {

    fun getYourGists(username: String, dataProvider: GitHubService): LivePagedListProvider<Int, Gist> {
        return object : LivePagedListProvider<Int, Gist>() {
            override fun createDataSource(): GistTiledRemoteDataSource<Gist> = object : GistTiledRemoteDataSource<Gist>(username, dataProvider) {
                override fun convertToItems(items: ArrayList<Gist>?): ArrayList<Gist>? {
                    return items
                }
            }
        }
    }

    fun getStarredGists(dataProvider: GitHubService): LivePagedListProvider<Int, Gist> {
        return object : LivePagedListProvider<Int, Gist>() {
            override fun createDataSource(): StarredGistTiledRemoteDataSource<Gist> = object : StarredGistTiledRemoteDataSource<Gist>(dataProvider) {
                override fun convertToItems(items: ArrayList<Gist>?): ArrayList<Gist>? {
                    return items
                }
            }
        }
    }

    fun getFollowerGists(followerName: String, dataProvider: GitHubService): LivePagedListProvider<Int, Gist> {
        return object : LivePagedListProvider<Int, Gist>() {
            override fun createDataSource(): GistTiledRemoteDataSource<Gist> = object : GistTiledRemoteDataSource<Gist>(followerName, dataProvider) {
                override fun convertToItems(items: ArrayList<Gist>?): ArrayList<Gist>? {
                    return items
                }
            }
        }
    }
}

abstract class GistTiledRemoteDataSource<T> protected constructor(val owner: String, val dataProvider: GitHubService) : TiledDataSource<T>() {
    protected abstract fun convertToItems(items: ArrayList<Gist>?): ArrayList<T>?

    override fun countItems(): Int = DataSource.COUNT_UNDEFINED

    override fun loadRange(startPosition: Int, loadCount: Int): List<T>? {
        val response: Response<java.util.ArrayList<Gist>>? = dataProvider.getGists(owner, (startPosition / Constants.PAGED_LIST_PAGE_SIZE).toString(), loadCount.toString()).execute()
        return convertToItems(response?.body())
    }
}

abstract class StarredGistTiledRemoteDataSource<T> protected constructor(val dataProvider: GitHubService) : TiledDataSource<T>() {
    protected abstract fun convertToItems(items: ArrayList<Gist>?): ArrayList<T>?

    override fun countItems(): Int = DataSource.COUNT_UNDEFINED

    override fun loadRange(startPosition: Int, loadCount: Int): List<T>? {
        val response: Response<java.util.ArrayList<Gist>>? = dataProvider.getYourStarredGists(startPosition.toString(), loadCount.toString()).execute()
        return convertToItems(response?.body())
    }
}

