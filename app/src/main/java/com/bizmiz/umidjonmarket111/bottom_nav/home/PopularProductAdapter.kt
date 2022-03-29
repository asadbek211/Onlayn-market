package com.bizmiz.umidjonmarket111.bottom_nav.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.databinding.PopularItemBinding
import com.bizmiz.umidjonmarket111.models.ExclusiveItem

class PopularProductAdapter : RecyclerView.Adapter<PopularProductAdapter.ViewHolder>() {
    var popularProductList: ArrayList<ExclusiveItem> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var onclick:(position:Int)->Unit = {}
    fun onClickListener(onclick:(position:Int)->Unit){
        this.onclick = onclick
    }
    inner class ViewHolder(private val binding: PopularItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun getCategory(exclusiveItem: ExclusiveItem, position: Int) {
            binding.tvImages.setImageResource(exclusiveItem.exclusiveImg)
            binding.tvName.text = exclusiveItem.exclusiveName
            binding.tvPrice.text = exclusiveItem.exclusivePrice
            binding.btnFavourite.setOnClickListener {
                binding.btnFavourite.setImageResource(R.drawable.ic_baseline_favorite_on)
            }
            binding.container.setOnClickListener {
                onclick.invoke(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val popularItemBinding =
            PopularItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(popularItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getCategory(popularProductList[position], position)
    }

    override fun getItemCount(): Int = popularProductList.size
}