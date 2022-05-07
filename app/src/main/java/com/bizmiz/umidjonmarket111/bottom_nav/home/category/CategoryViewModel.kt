package com.bizmiz.umidjonmarket111.bottom_nav.home.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bizmiz.umidjonmarket111.utils.Resource
import com.bizmiz.umidjonmarket111.models.CategoryItem

class CategoryViewModel(private val categoryDataHelper: CategoryDataHelper) : ViewModel() {
    private val categoryData: MutableLiveData<Resource<List<CategoryItem>>> = MutableLiveData()
    val category: LiveData<Resource<List<CategoryItem>>>
        get() = categoryData

    fun getCategoryData(
    ) {
        categoryDataHelper.getCategory(
            { success ->
                categoryData.value = Resource.success(success)
            },
            { failure ->
                categoryData.value = Resource.error(failure)
            })
    }
}