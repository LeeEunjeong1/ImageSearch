package com.example.imagesearch.model

data class ImageSaveData (
    val thumbnail_url: String,	// 미리보기 이미지 URL
    val datetime: String,   //문서 작성시간
    var is_save : Boolean // 이미지 저장 여부
)