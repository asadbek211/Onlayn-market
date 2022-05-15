package com.bizmiz.umidjonmarket111.bottom_nav.home

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.databinding.PopularItemBinding
import com.bizmiz.umidjonmarket111.models.ProductsItem
import com.bizmiz.umidjonmarket111.utils.Constant
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

class PopularProductAdapter : RecyclerView.Adapter<PopularProductAdapter.ViewHolder>() {
    var popularProductList: List<ProductsItem> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var onclick: (productsItem: ProductsItem) -> Unit = {}
    fun onClickListener(onclick: (productsItem: ProductsItem) -> Unit) {
        this.onclick = onclick
    }

    inner class ViewHolder(private val binding: PopularItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val prefs: SharedPreferences = binding.root.context.getSharedPreferences(
            Constant.PREFS,
            Context.MODE_PRIVATE
        )
        private val editor: SharedPreferences.Editor = prefs.edit()
        fun getCategory(productsItem: ProductsItem) {
            Glide.with(binding.root.context).load(productsItem.image_url).into(binding.tvImages)
            binding.tvName.text = productsItem.name
            binding.type.text = "1${productsItem.type} narxi"
            binding.tvPrice.text = "${productsItem.startPrice} UZS"
            binding.tvNewPrice.text = "${productsItem.endPrice} UZS"
            binding.tvOffer.text = productsItem.percent
            if (prefs.contains(productsItem.id)){
                binding.btnFavourite.setImageResource(R.drawable.ic_baseline_favorite_on)
            }else{
                binding.btnFavourite.setImageResource(R.drawable.ic_baseline_favorite_24)
            }
            binding.btnFavourite.setOnClickListener {
                if (prefs.contains(productsItem.id)){
                    binding.btnFavourite.setImageResource(R.drawable.ic_baseline_favorite_24)
                    editor.remove(productsItem.id).apply()
                    Snackbar.make(binding.root,"Saralanganlardan o'chirildi", Snackbar.LENGTH_SHORT).show()
                }else{
                    binding.btnFavourite.setImageResource(R.drawable.ic_baseline_favorite_on)
                    editor.putBoolean(productsItem.id,true).apply()
                    Snackbar.make(binding.root,"Saralanganlarga qo'shildi", Snackbar.LENGTH_SHORT).show()
                }
            }
            binding.container.setOnClickListener {
                onclick.invoke(productsItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val popularItemBinding =
            PopularItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(popularItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getCategory(popularProductList[position])
    }

    override fun getItemCount(): Int = popularProductList.size
}