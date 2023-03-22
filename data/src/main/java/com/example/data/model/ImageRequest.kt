package com.example.data.model

data class ImageRequest(
    val query: String, //	검색을 원하는 질의어	O
    val sort: String, //	결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy	X
    val page: Int, //	결과 페이지 번호, 1~50 사이의 값, 기본 값 1	X
    val size: Int, //	한 페이지에 보여질 문서 수, 1~80 사이의 값, 기본 값 80
)