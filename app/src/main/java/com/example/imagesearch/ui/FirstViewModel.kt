package com.example.imagesearch.ui

import androidx.lifecycle.ViewModel
import com.example.domain.usecase.RequestImageListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ImageRequest
import com.example.domain.utils.ErrorType
import com.example.domain.utils.RemoteErrorEmitter
import com.example.domain.utils.Util
import com.example.imagesearch.utils.ApiClient
import kotlinx.coroutines.launch

@HiltViewModel
class FirstViewModel @Inject constructor(
    private val requestImageListUseCase: RequestImageListUseCase
) : ViewModel(),RemoteErrorEmitter{
    override fun onError(errorType: ErrorType) {
        Util.logMessage("onError :: $errorType")
    }

    override fun onError(msg: String) {
        Util.logMessage("onError :: $msg")
    }
    fun initList(query:String,sort:String,page:Int,size:Int){
        val imageRequest = ImageRequest(query,sort,page,size)
        viewModelScope.launch {
            val response = requestImageListUseCase.excute(this@FirstViewModel, ApiClient.KAKAO_RESTAPI_KEY, imageRequest)
            Util.logMessage("response :: $response")
        }

    }
}