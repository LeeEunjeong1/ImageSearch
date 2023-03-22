package com.example.data.mapper

import com.example.data.model.ImageData
import com.example.domain.model.ImageListDataResponse
import com.example.data.model.ResponseResult
import com.example.domain.model.Image
import com.example.domain.model.Meta

object Mapper {
    fun mapperToImageList(imageDataList: ResponseResult<ImageData>): ImageListDataResponse {
        val imageList: MutableList<Image> = mutableListOf()
        imageDataList.documents!!.forEach {
            imageList.add(
                Image(
                    thumbnail_url = it.thumbnail_url,
                    datetime = it.datetime
                )
            )
        }

        return ImageListDataResponse(
            meta = Meta(
                total_count = imageDataList.meta.total_count,
                pageable_count = imageDataList.meta.pageable_count,
                is_end = imageDataList.meta.is_end
            ),
            documents = imageList
        )
    }
}