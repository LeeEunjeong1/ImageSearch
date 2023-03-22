package com.example.domain.model

data class ImageListDataResponse(
    val meta : Meta,
    val documents: List<Image>
)
data class Meta(
    val total_count: Int, // 검색 문서 수
    val pageable_count: Int,// total_count 중 노출 가능 문서 수
    val is_end: Boolean // 현재 페이지가 마지막 페이지인지 여부, 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음
)
data class Image (
    val thumbnail_url: String,	// 미리보기 이미지 URL
    val datetime: String    //문서 작성시간
)