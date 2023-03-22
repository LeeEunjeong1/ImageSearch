package com.example.data.repository

import com.example.data.api.ApiInterface
import com.example.data.model.ImageData
import com.example.data.model.ResponseResult
import com.example.domain.model.ImageRequest
import com.example.data.utils.BaseRepository
import com.example.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val api: ApiInterface
) : BaseRepository(), RemoteDataSource{
    override suspend fun getImageList(
        remoteErrorEmitter: RemoteErrorEmitter,
        key: String,
        imageRequest: ImageRequest
    ): ResponseResult<ImageData>? {
        return safeApiCallData(remoteErrorEmitter) {
            api.getImageList(
                key = key,
                query = imageRequest.query,
                sort = imageRequest.sort,
                page = imageRequest.page,
                size = imageRequest.size
            )
        }
    }
}