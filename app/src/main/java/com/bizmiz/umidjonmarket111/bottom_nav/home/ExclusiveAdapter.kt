package com.bizmiz.umidjonmarket111.bottom_nav.home

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.databinding.HomeExclusiveItemBinding
import com.bizmiz.umidjonmarket111.models.ProductsItem
import com.bizmiz.umidjonmarket111.utils.Constant
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

class ExclusiveAdapter : RecyclerView.Adapter<ExclusiveAdapter.ViewHolder>() {
    var exclusiveList: List<ProductsItem> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var onclick: (productsItem: ProductsItem) -> Unit = {}
    fun onClickListener(onclick: (productsItem: ProductsItem) -> Unit) {
        this.onclick = onclick
    }
    private var favourite: (status:String) -> Unit = {}
    fun onFavouriteListener(favourite: (status:String) -> Unit) {
        this.favourite = favourite
    }
    inner class ViewHolder(private val binding: HomeExclusiveItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val prefs: SharedPreferences = binding.root.context.getSharedPreferences(
            Constant.PREFS,
            Context.MODE_PRIVATE
        )
        private val editor: SharedPreferences.Editor = prefs.edit()
        fun getCategory(productsItem: ProductsItem, position: Int) {
            Glide.with(binding.root.context).load(productsItem.image_url).into(binding.tvImages)
            binding.tvName.text = productsItem.name
            binding.type.text = "1${productsItem.type} narxi"
            binding.tvPrice.text = "${productsItem.startPrice} UZS"
            if (productsItem.endPrice != "") {
                binding.tvNewPrice.text = "${productsItem.endPrice} UZS"
                binding.tvOffer.text = productsItem.percent
                binding.tvNewPrice.visibility = View.VISIBLE
                binding.offerBackground.visibility = View.VISIBLE
                binding.tvOffer.visibility = View.VISIBLE
                binding.tvLine.visibility = View.VISIBLE

            } else {
                binding.tvNewPrice.visibility = View.GONE
                binding.offerBackground.visibility = View.GONE
                binding.tvOffer.visibility = View.GONE
                binding.tvLine.visibility = View.GONE
            }
            if (prefs.contains(productsItem.id)){
                binding.btnFavourite.setImageResource(R.drawable.ic_baseline_favorite_on)
            }else{
                binding.btnFavourite.setImageResource(R.drawable.ic_baseline_favorite_24)
            }
            binding.btnFavourite.setOnClickListener {
                if (prefs.contains(productsItem.id)){
                    binding.btnFavourite.setImageResource(R.drawable.ic_baseline_favorite_24)
                    editor.remove(productsItem.id).apply()
                    favourite.invoke("removed")
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
        val homeExclusiveItemBinding =
            HomeExclusiveItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(homeExclusiveItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getCategory(exclusiveList[position], position)
    }

    override fun getItemCount(): Int = exclusiveList.size
}