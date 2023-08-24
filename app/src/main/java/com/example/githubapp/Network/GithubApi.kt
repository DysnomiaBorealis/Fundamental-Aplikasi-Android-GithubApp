package com.example.githubapp.Network

import com.example.githubapp.DataClass.SearchResult
import com.example.githubapp.DataClass.User
import com.example.githubapp.DataClass.UserDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {
    @GET("search/users")
    suspend fun searchUsers(@Query("q") query: String): Response<SearchResult>

    @GET("users/{username}")
    suspend fun getUserDetail(@Path("username") username: String): Response<UserDetail>

    @GET("users/{username}/followers")
    suspend fun getFollowers(@Path("username") username: String): Response<List<User>>

    @GET("users/{username}/following")
    suspend fun getFollowing(@Path("username") username: String): Response<List<User>>
}