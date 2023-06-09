package com.example.githubusersubmission1.epiay

import com.example.githubusersubmission1.data.ResponseUserLists
import com.example.githubusersubmission1.data.ResponseDetailUser
import com.example.githubusersubmission1.data.ResponseUsersSearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    companion object {
        const val API_KEY = "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"
    }
    @GET("users")
    @Headers("Authorization: token $API_KEY")
    fun getUsers(): Call<ArrayList<ResponseUserLists>>

    @GET("search/users")
    @Headers("Authorization: token $API_KEY")
    fun getSearchUsers( @Query("q") username: String): Call<ResponseUsersSearch>

    @GET("users/{username}")
    @Headers("Authorization: token $API_KEY")
    fun getDetailUser(@Path("username") username: String) : Call<ResponseDetailUser>

    @GET("users/{username}/followers")
    @Headers("Authorization: token $API_KEY")
    fun getFollowers(@Path("username") username: String) : Call<ArrayList<ResponseUserLists>>

    @GET("users/{username}/following")
    @Headers("Authorization: token $API_KEY")
    fun getFollowings(@Path("username") username: String) : Call<ArrayList<ResponseUserLists>>
}