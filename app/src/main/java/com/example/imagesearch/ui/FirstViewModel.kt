package com.example.imagesearch.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Image
import com.example.domain.model.ImageListDataResponse
import com.example.domain.model.ImageRequest
import com.example.domain.usecase.RequestImageListUseCase
import com.example.domain.usecase.RequestVideoListUseCase
import com.example.domain.utils.ErrorType
import com.example.domain.utils.RemoteErrorEmitter
import com.example.domain.utils.Util
import com.example.imagesearch.utils.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirstViewModel @Inject constructor(
    private val requestImageListUseCase: RequestImageListUseCase,
    private val requestVideoListUseCase: RequestVideoListUseCase
) : ViewModel(), RemoteErrorEmitter {
    val imageListData = MutableLiveData<List<Image>>()
    private val imageIsEnd = MutableLiveData(false)
    private val videoIsEnd = MutableLiveData(false)
    val allIsEnd = MutableLiveData(false)
    val emptyList = MutableLiveData<Boolean>()

    override fun onError(errorType: ErrorType) {
        Util.logMessage("onError :: $errorType")
    }

    override fun onError(msg: String) {
        Util.logMessage("onError :: $msg")
    }

    // 초기화 함수
    fun reset() {
        imageListData.value = mutableListOf()
        imageIsEnd.value = false
        videoIsEnd.value = false
        allIsEnd.value = false
    }

    // 이미지 리스트 요청 함수
    fun getImageList(query: String, sort: String, page_cnt: Int, size: Int) {
        val imageRequest = ImageRequest(query, sort, page_cnt, size)
        viewModelScope.launch {

            // 이미지 리스트 요청 및 응답값 저장
            val imageList = mutableListOf<Image>()
            var imageResponse: List<Image>? = null
            var videoResponse: List<Image>? = null

            if (imageIsEnd.value != true) {
                imageResponse = requestImageList("IMAGE", imageRequest)
            }
            if (videoIsEnd.value != true) {
                videoResponse = requestImageList("VIDEO", imageRequest)
            }
            // 응답값 이미지 리스트 에 저장
            if (imageResponse != null) {
                imageList.addAll(imageResponse.toList())
                FirstFragment.cnt += imageResponse.size
            } else if (videoResponse != null) {
                imageList.addAll(videoResponse.toList())
                FirstFragment.cnt += videoResponse.size
            } else {
                emptyList.postValue(true) // 응답값 없으면 emptyList
            }

            // 두 리스트 모두 마지막 요청일 경우 -> 더이상 요청X
            if (imageIsEnd.value == true && videoIsEnd.value == true) {
                allIsEnd.postValue(true)
            } else {
                allIsEnd.postValue(false)
            }
            // 이미지 리스트 정렬 ( datetime 기준 - 최신순)
            imageList.sortWith(compareByDescending { it.datetime })

            // 이미지 리스트 -> 기존 리스트 에 추가
            imageList.let {
                val temp = arrayListOf<Image>().apply {
                    imageListData.value?.let { data -> addAll(data) }
                    addAll(it)
                }
                imageListData.postValue(temp)
            }
        }
    }

    // 이미지 요청 함수
    private suspend fun requestImageList(type: String, requestData: ImageRequest): List<Image>? {
        // 이미지/비디오 요청
        val imageResponse: ImageListDataResponse? = if (type == "IMAGE") {
            requestImageListUseCase.excute(
                this@FirstViewModel,
                ApiClient.KAKAO_RESTAPI_KEY,
                requestData
            )
        } else {
            requestVideoListUseCase.excute(
                this@FirstViewModel,
                ApiClient.KAKAO_RESTAPI_KEY,
                requestData
            )
        }
        Util.logMessage("imageResponse :: $imageResponse")
        return if (imageResponse != null && imageResponse.meta.pageable_count != 0) {
            imageIsEnd.value = imageResponse.meta.is_end
            imageResponse.documents
        } else {
            imageIsEnd.value = true
            null
        }
    }
}