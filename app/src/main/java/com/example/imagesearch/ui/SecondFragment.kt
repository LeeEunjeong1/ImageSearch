package com.example.imagesearch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imagesearch.databinding.FragmentSecondBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SecondViewModel>()
    private lateinit var imageListAdapter: ImageSaveListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        viewModel.getImageList()
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

    private fun initAdapter() {
        val layoutManager = LinearLayoutManager(context)
        imageListAdapter = ImageSaveListAdapter()
        with(binding) {
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = imageListAdapter
        }
    }

    private fun initObserve() {
        with(viewModel) {
            imageListData.observe(viewLifecycleOwner) {
                imageListAdapter.setImage(it)
            }
        }
    }

}