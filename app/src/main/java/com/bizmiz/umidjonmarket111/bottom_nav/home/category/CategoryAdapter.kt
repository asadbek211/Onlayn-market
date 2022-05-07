package com.bizmiz.umidjonmarket111.bottom_nav.home.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bizmiz.umidjonmarket111.databinding.HomeCategoryItemBinding
import com.bizmiz.umidjonmarket111.models.CategoryItem
import com.bumptech.glide.Glide

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    var categoryList: List<CategoryItem> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var onclick:(categoryItem: CategoryItem)->Unit = {}
    fun onClickListener(onclick:(categoryItem: CategoryItem)->Unit){
        this.onclick = onclick
    }
    inner class ViewHolder(private val binding: HomeCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun getCategory(categoryItem: CategoryItem, position: Int) {
            Glide.with(binding.root.context)
                .load(categoryItem.image_url)
                .into(binding.tvImages)
            binding.tvName.text = categoryItem.name
            binding.linerCompatContainer.setOnClickListener {
                onclick.invoke(categoryItem)
            }
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