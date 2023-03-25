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
import com.example.domain.utils.Util
import com.example.imagesearch.databinding.FragmentFirstBinding
import com.example.imagesearch.utils.PrefUtil
import com.google.gson.Gson
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
            editSearch.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    btnCancel.visibility = View.VISIBLE
                } else {
                    btnCancel.visibility = View.GONE
                }
            }
            editSearch.setOnKeyListener { _, keyCode, event ->
                if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) && editSearch.text.toString() != "") {
                    // 초기화 후 검색
                    viewModel.reset()
                    page = 1
                    cnt = 0
                    viewModel.getImageList(editSearch.text.toString(), "recency", page, size)
                    hideKeyboard()
                    true
                } else {
                    false
                }
            }
            btnCancel.setOnClickListener {
                hideKeyboard()
            }

        }
    }

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(context)
        imageListAdapter = ImageListAdapter(onClickImage = { imageData ->
            val saveList = mutableListOf<String>()
            val prefList = PrefUtil(requireContext()).getString("SAVE_IMAGE")
            if (prefList.isNotEmpty()) {
                val list = Gson().fromJson(prefList, List::class.java)
                list.forEach {
                    saveList.add(it.toString())
                }
            }
            saveList.add(imageData.toString())
            PrefUtil(requireContext()).setString("SAVE_IMAGE", Gson().toJson(saveList))
        }
        )
        with(binding) {
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = imageListAdapter
        }
        recyclerViewScrollListener()
    }

    private fun initObserve() {
        with(viewModel) {
            imageListData.observe(viewLifecycleOwner) {
                Util.logMessage("it :: $it")
                if (it.isNotEmpty()) {
                    imageListAdapter.setImage(it)

                }
            }

        }

    }

    private fun recyclerViewScrollListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = binding.recyclerView.layoutManager

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() // 화면에 보이는 마지막 아이템의 position
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1 // 어댑터에 등록된 아이템의 총 개수 -1

                // recyclerView 스크롤 안될때 (최하단 감지)
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
        var size: Int = 40
        var cnt: Int = 0
    }
}