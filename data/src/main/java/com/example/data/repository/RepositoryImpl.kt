package com.example.data.repository

import com.example.data.mapper.Mapper
import com.example.domain.model.ImageListDataResponse
import com.example.domain.model.ImageRequest
import com.example.domain.repository.Repository
import com.example.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
): Repository {
    override suspend fun getImageList(
        remoteErrorEmitter: RemoteErrorEmitter,
        key: String,
        imageRequest: ImageRequest
    ): ImageListDataResponse? {
        val responseResult = remoteDataSource.getImageList(remoteErrorEmitter, key, imageRequest)
        return responseResult?.let { Mapper.mapperToImageList(it) }
    }
}