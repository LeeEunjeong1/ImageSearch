package com.example.domain.model

data class ImageRequest(
    val query: String,
    val sort: String,
    val page: Int,
    val size: Int,
)