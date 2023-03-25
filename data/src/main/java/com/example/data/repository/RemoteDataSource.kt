package com.example.data.repository

import com.example.data.model.ImageData
import com.example.data.model.ResponseResult
import com.example.data.model.VideoData
import com.example.domain.utils.RemoteErrorEmitter

interface RemoteDataSource {
    suspend fun getImageList(
        remoteErrorEmitter: RemoteErrorEmitter,
        key:String, imageRequest: com.example.domain.model.ImageRequest
    ): ResponseResult<ImageData>?
    suspend fun getVideoList(
        remoteErrorEmitter: RemoteErrorEmitter,
        key:String, imageRequest: com.example.domain.model.ImageRequest
    ): ResponseResult<VideoData>?
}