package com.example.data.model

data class ResponseResult<T>(
    val meta : Meta,
    val documents: List<T>? = null
)
