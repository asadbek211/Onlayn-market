package com.bizmiz.umidjonmarket111.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bizmiz.umidjonmarket111.databinding.HomeCategoryItemBinding
import com.bizmiz.umidjonmarket111.ui.home.model.CategoryItem

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    var categoryList: ArrayList<CategoryItem> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val binding: HomeCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun getCategory(categoryItem: CategoryItem, position: Int) {
            binding.tvImages.setImageResource(categoryItem.categoryImg)
            binding.tvName.text = categoryItem.categoryText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val homeCategoryItemBinding =
            HomeCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(homeCategoryItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getCategory(categoryList[position], position)
    }

    override fun getItemCount(): Int = categoryList.size
}