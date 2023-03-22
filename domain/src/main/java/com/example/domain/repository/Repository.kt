package com.example.domain.repository

import com.example.domain.model.ImageListDataResponse
import com.example.domain.model.ImageRequest
import com.example.domain.utils.RemoteErrorEmitter

interface Repository {
    suspend fun getImageList(remoteErrorEmitter: RemoteErrorEmitter, key: String, imageRequest:ImageRequest): ImageListDataResponse?
}