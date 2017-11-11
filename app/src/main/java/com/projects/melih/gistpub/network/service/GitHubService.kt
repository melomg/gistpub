package com.projects.melih.gistpub.network.service

import com.projects.melih.gistpub.model.Contributor
import com.projects.melih.gistpub.model.Gist
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url
import java.util.*

/**
 * Created by melihmg on 19/02/2017
 */

interface GitHubService {
    @GET("user")
    fun login(): Call<Contributor>

    @GET("repos/{owner}/{repo}/contributors")
    fun getRepoContributors(@Path("owner") owner: String, @Path("repo") repo: String): Call<List<Contributor>>

    @GET("users/{owner}/gists")
    fun getGists(@Path("owner") owner: String, @Query("page") pageIndex: String, @Query("per_page") paginationCount: String): Call<ArrayList<Gist>>

    @GET("users/{owner}/followers")
    fun getYourFollowers(@Path("owner") owner: String, @Query("page") pageIndex: String, @Query("per_page") paginationCount: String): Call<ArrayList<Contributor>>

    @GET("users/{owner}/gists")
    fun searchCode(@Path("owner") owner: String): Call<ArrayList<Gist>>

    @GET("gists/starred")
    fun getYourStarredGists(@Query("page") pageIndex: String, @Query("per_page") paginationCount: String): Call<ArrayList<Gist>>

    @GET
    fun downloadFileWithDynamicUrlSync(@Url fileUrl: String): Call<ResponseBody>
}
