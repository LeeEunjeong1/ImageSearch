package com.example.imagesearch.ui

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.utils.Util
import com.example.imagesearch.databinding.ImageListItemBinding
import com.example.imagesearch.model.ImageSaveData
import com.example.imagesearch.utils.StringUtil
import kotlin.math.ceil

class ImageSaveListAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemList = ArrayList<ImageSaveData>()

    // 리스트 초기화
    fun clearList(){
        this.itemList.clear()
    }
    // 리스트 추가
    fun setImage(itemList: List<ImageSaveData>) {
        clearList()
        itemList.forEach {
            this.itemList.add(it)
        }
        notifyDataSetChanged()
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageHolder(ImageListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    fun getItemData(position: Int, num:Int): ImageSaveData?{
        val findIndex = (position*4)+num
        return if(findIndex < itemList.size){
            itemList[findIndex]
        }else{
            null
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Util.logMessage("onBindViewHolder $position")
        if (holder is ImageHolder) {
//            holder.bind(itemList[position])
            val item1 = getItemData(position, 0)
            val item2 = getItemData(position, 1)
            val item3 = getItemData(position, 2)
            val item4 = getItemData(position, 3)
            holder.bind(item1, item2, item3, item4)
        }
    }
    inner class ImageHolder(val binding: ImageListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item1: ImageSaveData?, item2: ImageSaveData?, item3: ImageSaveData?, item4: ImageSaveData?) {
            initImage(binding.image1, binding.dateTime1, item1)
            initImage(binding.image2, binding.dateTime2, item2)
            initImage(binding.image3, binding.dateTime3, item3)
            initImage(binding.image4, binding.dateTime4, item4)
        }

        fun initImage(imageView: ImageView, dateTimeTextView: TextView, item: ImageSaveData?){
            if(item == null){
                Glide.with(itemView).clear(imageView)
            }else{
                item.let { imageData ->
                    val uri = imageData.thumbnail_url
                    Glide.with(itemView).load(uri).into(imageView)
                    dateTimeTextView.text = StringUtil.dateFormat(imageData.datetime.substring(0,19), "yyyy.MM.dd\nHH시 mm분")
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return ceil(itemList.size.toDouble()/4).toInt()
    }
}