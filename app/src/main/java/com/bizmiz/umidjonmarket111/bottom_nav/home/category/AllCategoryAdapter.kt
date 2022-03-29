package com.bizmiz.umidjonmarket111.bottom_nav.home.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bizmiz.umidjonmarket111.databinding.AllCategoryItemBinding
import com.bizmiz.umidjonmarket111.models.CategoryItem

class AllCategoryAdapter : RecyclerView.Adapter<AllCategoryAdapter.ViewHolder>() {
    var allCategoryList: ArrayList<CategoryItem> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var onclick: (position: Int) -> Unit = {}
    fun onClickListener(onclick: (position: Int) -> Unit) {
        this.onclick = onclick
    }

    inner class ViewHolder(private val binding: AllCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun getCategory(categoryItem: CategoryItem, position: Int) {
            binding.categoryImages.setImageResource(categoryItem.categoryImg)
            binding.tvCategoryName.text = categoryItem.categoryText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val allCategoryItemBinding =
            AllCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(allCategoryItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getCategory(allCategoryList[position], position)
    }

    override fun getItemCount(): Int = allCategoryList.size
}