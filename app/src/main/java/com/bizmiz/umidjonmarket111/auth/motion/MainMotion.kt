package com.bizmiz.umidjonmarket111.auth.motion

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bizmiz.umidjonmarket111.R
import com.bizmiz.umidjonmarket111.databinding.FragmentMainMotionBinding

class MainMotion : Fragment() {
    private lateinit var binding: FragmentMainMotionBinding
    private var adapter: OnBoardingAdapter? = null
    private var running = false
    private lateinit var prefs: SharedPreferences
    private var fromPositionToLeft: Animation? = null
    private var fromRightToPosition: Animation? = null
    private var fromLeftToPosition: Animation? = null
    private var fromPositionToRight: Animation? = null
    private var hide: Animation? = null
    private var show: Animation? = null

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        prefs = requireActivity().getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE)
        if (getPrefs()==1){
            val navController =
                Navigation.findNavController(requireActivity(), R.id.authContainer)
            navController.navigate(R.id.action_mainMotion_to_updateAccountFragment2)
        }
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.first_color)
        requireActivity().window.decorView.windowInsetsController?.setSystemBarsAppearance(
            0,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
        if (!::binding.isInitialized){
            binding = FragmentMainMotionBinding.bind(inflater.inflate(R.layout.fragment_main_motion, container, false))
        }
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireActivity(), R.color.white)
        requireActivity().window.decorView.windowInsetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
        initViewPager()
        return binding.root
    }
    private fun initViewPager() {
        fromPositionToRight =
            AnimationUtils.loadAnimation(requireActivity(), R.anim.from_position_to_right_300)
        fromPositionToLeft =
            AnimationUtils.loadAnimation(requireActivity(), R.anim.from_position_to_left_300)
        fromRightToPosition =
            AnimationUtils.loadAnimation(requireActivity(), R.anim.from_right_to_position_300)
        fromLeftToPosition =
            AnimationUtils.loadAnimation(requireActivity(), R.anim.from_left_to_position_300)
        hide = AnimationUtils.loadAnimation(requireActivity(), R.anim.alpha_hide)
        show = AnimationUtils.loadAnimation(requireActivity(), R.anim.alpha_show)
        setItems()
        binding.viewpager.adapter = adapter
        binding.indicators.setViewPager2(binding.viewpager)
        binding.viewpager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 2) {
                    if (!running) {
                        binding.apply {
                            textSkip.startAnimation(hide)
                            textSkip.visibility = View.GONE
                            textNext.startAnimation(fromPositionToLeft)
                            textNext.visibility = View.GONE
                            textAuthorize.visibility = View.VISIBLE
                            textAuthorize.startAnimation(fromRightToPosition)
                        }
                        running = true
                    }
                } else {
                    if (running) {
                        binding.apply {
                            textSkip.visibility = View.VISIBLE
                            textSkip.startAnimation(show)
                            textAuthorize.startAnimation(fromPositionToLeft)
                            textAuthorize.visibility = View.INVISIBLE
                            textNext.visibility = View.VISIBLE
                            textNext.startAnimation(fromRightToPosition)
                        }
                        running = false
                    }
                }
            }
        })
        binding.textNext.setOnClickListener {
            if (binding.viewpager.currentItem + 1 < adapter!!.itemCount) {
                binding.viewpager.currentItem = binding.viewpager.currentItem + 1
            }
        }
        binding.textAuthorize.setOnClickListener {
            if (binding.viewpager.currentItem == 2) {
                if (binding.motionLayout.progress.toDouble() == 0.0) {
                    val navController = Navigation.findNavController(requireActivity(), R.id.authContainer)
                    navController.navigate(R.id.action_mainMotion_to_createAccount)
                }
            }
        }
        binding.textSkip.setOnClickListener {
            binding.viewpager.currentItem = adapter!!.itemCount
        }
    }
    private fun setItems() {
        val onBoardingItems: MutableList<OnBoardingItem> = ArrayList()
        val item1 = OnBoardingItem()
        item1.title = resources.getString(R.string.onboarding_page1_title)
        item1.title2 = resources.getString(R.string.onboarding_page1_title2)
        item1.description = resources.getString(R.string.onboarding_page1_desription)
        item1.image = R.drawable.onboarding_image1
        val item2 = OnBoardingItem()
        item2.title = resources.getString(R.string.onboarding_page2_title)
        item2.title2 = resources.getString(R.string.onboarding_page2_title2)
        item2.description = resources.getString(R.string.onboarding_page2_desription)
        item2.image = R.drawable.onboarding_image2
        val categoryItem5 = OnBoardingItem()
        categoryItem5.title = (resources.getString(R.string.onboarding_page5_title))
        categoryItem5.title2 = (resources.getString(R.string.onboarding_page5_title2))
        categoryItem5.description = (resources.getString(R.string.onboarding_page5_desription))
        categoryItem5.image = (R.drawable.onboarding_image5)
        onBoardingItems.add(item1)
        onBoardingItems.add(item2)
        onBoardingItems.add(categoryItem5)
        adapter = OnBoardingAdapter(onBoardingItems)
    }
    private fun getPrefs(): Int {
        prefs = requireActivity().getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE)
        return prefs.getInt("number", 0)
    }
}