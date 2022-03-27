package com.bizmiz.umidjonmarket111.auth.motion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bizmiz.umidjonmarket111.databinding.MotionOnboardingItemBinding

class OnBoardingAdapter(val list:MutableList<OnBoardingItem>) : RecyclerView.Adapter<OnBoardingAdapter.ViewHolder>() {

    var items = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val motionOnboardingItemBinding =
            MotionOnboardingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(motionOnboardingItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(val binding: MotionOnboardingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(item: OnBoardingItem) {
            binding.onboardingItemTitle.text = item.title
            binding.onboardingItemTitle2.text = item.title2
            binding.onboardingItemDescription.text = item.description
            binding.onboardingItemImage.setImageResource(item.image)
        }
    }
}