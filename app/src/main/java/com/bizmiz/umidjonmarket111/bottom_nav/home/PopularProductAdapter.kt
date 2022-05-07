package com.bizmiz.umidjonmarket111.bottom_nav.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.databinding.PopularItemBinding
import com.bizmiz.umidjonmarket111.models.ProductsItem
import com.bumptech.glide.Glide

class PopularProductAdapter : RecyclerView.Adapter<PopularProductAdapter.ViewHolder>() {
    var popularProductList: List<ProductsItem> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var onclick: (position: Int) -> Unit = {}
    fun onClickListener(onclick: (position: Int) -> Unit) {
        this.onclick = onclick
    }

    inner class ViewHolder(private val binding: PopularItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun getCategory(productsItem: ProductsItem, position: Int) {
            Glide.with(binding.root.context).load(productsItem.image_url).into(binding.tvImages)
            binding.tvName.text = productsItem.name
            binding.type.text = "1${productsItem.type} narxi"
            binding.tvPrice.text = "${productsItem.startPrice} UZS"
            binding.tvNewPrice.text = "${productsItem.endPrice} UZS"
            binding.tvOffer.text = productsItem.percent
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