package com.bizmiz.umidjonmarket111.bottom_nav.home.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bizmiz.umidjonmarket111.databinding.AllCategoryItemBinding
import com.bizmiz.umidjonmarket111.models.CategoryItem
import com.bumptech.glide.Glide

class AllCategoryAdapter : RecyclerView.Adapter<AllCategoryAdapter.ViewHolder>() {
    var allCategoryList: List<CategoryItem> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var onclick: (categoryItem: CategoryItem) -> Unit = {}
    fun onClickListener(onclick: (categoryItem: CategoryItem) -> Unit) {
        this.onclick = onclick
    }

    inner class ViewHolder(private val binding: AllCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun getCategory(categoryItem: CategoryItem) {
            Glide.with(binding.root.context)
                .load(categoryItem.image_url)
                .into(binding.categoryImages)
            binding.tvCategoryName.text = categoryItem.name
            binding.linerCompatContainer.setOnClickListener {
                onclick.invoke(categoryItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val allCategoryItemBinding =
            AllCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(allCategoryItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getCategory(allCategoryList[position])
    }

    override fun getItemCount(): Int = allCategoryList.size
}