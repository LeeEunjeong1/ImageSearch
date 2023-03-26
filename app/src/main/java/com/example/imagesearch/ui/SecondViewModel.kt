package com.example.imagesearch.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imagesearch.model.ImageSaveData
import com.example.imagesearch.utils.PrefUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SecondViewModel @Inject constructor(
    private val prefUtil: PrefUtil
) : ViewModel() {
    val imageListData = MutableLiveData<List<ImageSaveData>>()

    // 저장된 이미지 리스트 가져오기
    fun getImageList() {
        val prefList = prefUtil.getString("SAVE_IMAGE")
        if (prefList.isNotEmpty()) {
            val gson = Gson()
            val arrayType = object : TypeToken<ArrayList<ImageSaveData>>() {}.type
            val data: ArrayList<ImageSaveData> = gson.fromJson(prefList, arrayType)
            imageListData.postValue(data)
        }
    }
}