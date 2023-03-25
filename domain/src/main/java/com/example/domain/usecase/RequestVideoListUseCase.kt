package com.example.domain.usecase

import com.example.domain.model.ImageListDataResponse
import com.example.domain.model.ImageRequest
import com.example.domain.repository.Repository
import com.example.domain.utils.RemoteErrorEmitter
import javax.inject.Inject

class RequestVideoListUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun excute(
        remoteErrorEmitter: RemoteErrorEmitter,
        key: String,
        imageRequest: ImageRequest
    ): ImageListDataResponse? {
        return repository.getVideoList(remoteErrorEmitter, key, imageRequest)
    }
}