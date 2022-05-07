package com.bizmiz.umidjonmarket111.bottom_nav.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bizmiz.umidjonmarket111.models.ProductsItem
import com.bizmiz.umidjonmarket111.utils.Resource

class ProductsViewModel(private val productDataHelper: ProductDataHelper) : ViewModel() {
    private val productsData: MutableLiveData<Resource<List<ProductsItem>>> = MutableLiveData()
    val products: LiveData<Resource<List<ProductsItem>>>
        get() = productsData
    private val productsData2: MutableLiveData<Resource<List<ProductsItem>>> = MutableLiveData()
    val productsByCategory: LiveData<Resource<List<ProductsItem>>>
        get() = productsData2

    fun getProductData(
    ) {
        productDataHelper.getProduct(
            { success ->
                productsData.value = Resource.success(success)
            },
            { failure ->
                productsData.value = Resource.error(failure)
            })
    }
    fun getProductDataByCategoryId(
        categoryId:String,
        productId:String,
    ) {
        productDataHelper.getProductByCategoryId(
            categoryId,
            productId,
            { success ->
                productsData2.value = Resource.success(success)
            },
            { failure ->
                productsData2.value = Resource.error(failure)
            })
    }
}