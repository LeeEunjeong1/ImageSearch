package com.example.imagesearch.ui

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imagesearch.databinding.FragmentFirstBinding
import com.example.imagesearch.model.ImageSaveData
import com.example.imagesearch.utils.PrefUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!
    private val viewModel by viewModels<FirstViewModel>()
    private lateinit var imageListAdapter: ImageListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        PrefUtil(requireContext()).setString("SEARCH_TEXT","") // 검색어 초기화
        initListener()
        initAdapter()
        initObserve()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener() {
        with(binding) {
            // editText 포커스 해제 -> 취소 버튼 GONE
            editSearch.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    btnCancel.visibility = View.VISIBLE
                } else {
                    btnCancel.visibility = View.GONE
                }
            }
            // editText 검색
            editSearch.setOnKeyListener { _, keyCode, event ->
                if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) && editSearch.text.toString() != "") {
                    // 초기화 후 검색
                    viewModel.reset()
                    page = 1
                    cnt = 0
                    viewModel.getImageList(editSearch.text.toString(), "recency", page, size)
                    hideKeyboard()
                    PrefUtil(requireContext()).setString("SEARCH_TEXT",editSearch.text.toString()) // 검색어 저장
                    true
                } else {
                    false
                }
            }
            // 취소 버튼
            btnCancel.setOnClickListener {
                editSearch.clearFocus()
                editSearch.setText(PrefUtil(requireContext()).getString("SEARCH_TEXT")) // 이전 검색어 유지
                hideKeyboard()
            }

        }
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(context)
        imageListAdapter = ImageListAdapter(
            onClickImage = { imageData -> // 이미지 클릭
            val saveList : ArrayList<ImageSaveData> = ArrayList() // 최종 저장할 이미지 리스트
            val prefList = PrefUtil(requireContext()).getString("SAVE_IMAGE") // 기존에 저장된 이미지 리스트
            if (prefList.isNotEmpty()) {
                val gson = Gson()
                val arrayType = object : TypeToken<ArrayList<ImageSaveData>>() {}.type
                val data: ArrayList<ImageSaveData> = gson.fromJson(prefList, arrayType)
                saveList.addAll(data) // 최종 저장 리스트 에 기존에 저장된 리스트 넣기
            }
            // 클릭한 이미작 이미 저장 되어 있는 경우 -> 최종 저장 리스트 에서 삭제
            if(imageData.is_save){
                saveList.removeAt(saveList.indexOf(imageData))
            }else{
                // 저장 되어 있지 않는 경우 -> 최종 저장 리스트 에 추가
                saveList.add(
                    ImageSaveData(
                        imageData.thumbnail_url,
                        imageData.datetime,
                        true
                    )
                )
            }
            PrefUtil(requireContext()).setString("SAVE_IMAGE", Gson().toJson(saveList))
        })
        with(binding) {
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = imageListAdapter
        }
        recyclerViewScrollListener()
    }

    private fun initObserve() {
        with(viewModel) {
            imageListData.observe(viewLifecycleOwner) { res ->
                val imageSaveList : ArrayList<ImageSaveData> = ArrayList() // 이미지 저장 플래그 추가할 리스트
                if (res.isNotEmpty()) {
                    // 이미지 리스트 저장 플래그 false 로 초기화
                    res.forEach{data ->
                        imageSaveList.add(
                            ImageSaveData(
                                data.thumbnail_url,
                                data.datetime,
                                false
                            )
                        )
                    }
                    // 저장된 이미지 리스트 불러오기
                    val prefList = PrefUtil(requireContext()).getString("SAVE_IMAGE")
                    if (prefList.isNotEmpty()) {
                        val gson = Gson()
                        val arrayType = object : TypeToken<ArrayList<ImageSaveData>>() {}.type
                        val data: ArrayList<ImageSaveData> = gson.fromJson(prefList, arrayType)
                        // 저장된 이미지 리스트 값 비교 -> 저장 플래그 true 로 변경
                        data.forEach{ pref->
                            imageSaveList.filter{pref.thumbnail_url==it.thumbnail_url}.forEach { it.is_save = true}
                        }
                    }
                    imageListAdapter.setImage(imageSaveList)
                }
            }

        }

    }

    // recyclerview 최하단 스크롤 감지 -> 다음 이미지 요청
    private fun recyclerViewScrollListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() // 화면에 보이는 마지막 아이템의 position
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1 // 어댑터에 등록된 아이템의 총 개수 -1

                // recyclerView 스크롤 안될때 (최하단 감지, 요청할 데이터가 남아있는 경우)
                if (!recyclerView.canScrollVertically(1) && dy > 0 && !viewModel.allIsEnd.value!! && lastVisibleItemPosition == itemTotalCount) {
                    page++
                    viewModel.getImageList(
                        binding.editSearch.text.toString(),
                        "recency",
                        page,
                        size
                    )
                }
            }
        })
    }

    // 키패드 내리기
    private fun hideKeyboard() {
        if (activity != null && requireActivity().currentFocus != null) {
            val inputManager: InputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                requireActivity().currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    companion object {
        var page: Int = 1
        var size: Int = 20
        var cnt: Int = 0
    }
}