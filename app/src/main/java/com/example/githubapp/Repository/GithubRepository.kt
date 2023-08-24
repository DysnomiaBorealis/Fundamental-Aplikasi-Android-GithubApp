package com.example.githubapp.Repository
import com.example.githubapp.Network.GithubApi
import com.example.githubapp.DataClass.SearchResult
import com.example.githubapp.DataClass.User
import com.example.githubapp.DataClass.UserDetail
import retrofit2.Response

class GithubRepository(private val api : GithubApi) {
    suspend fun searchUsers(query: String): Response<SearchResult> {
        return api.searchUsers(query)
    }

    suspend fun getUserDetail(username: String): Response<UserDetail> {
        return api.getUserDetail(username)
    }

    suspend fun getFollowers(username: String): Response<List<User>> {
        return api.getFollowers(username)
    }

    suspend fun getFollowing(username: String): Response<List<User>> {
        return api.getFollowing(username)
    }

}