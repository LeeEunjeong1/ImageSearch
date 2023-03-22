package com.example.data.repository

import com.example.data.model.ImageData
import com.example.data.model.ResponseResult
import com.example.domain.model.ImageRequest
import com.example.domain.utils.RemoteErrorEmitter

interface RemoteDataSource {
    suspend fun getImageList(remoteErrorEmitter: RemoteErrorEmitter,key:String, imageRequest: ImageRequest): ResponseResult<ImageData>?
}