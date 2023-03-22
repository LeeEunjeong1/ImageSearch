package com.example.data.api

import com.example.data.model.ImageData
import com.example.data.model.ResponseResult
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiInterface {
    @GET("v2/search/image")
    suspend fun getImageList(
        @Header("Authorization") key: String,
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): ResponseResult<ImageData>
}