package com.example.githubusersubmission1.epiay

import com.example.githubusersubmission1.data.ResponseUsersSearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers( @Query("q") username: String): Call<ResponseUsersSearch>
}