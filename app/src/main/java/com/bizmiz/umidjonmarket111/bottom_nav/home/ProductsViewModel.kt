package com.bizmiz.umidjonmarket111.bottom_nav.home

import android.content.SharedPreferences
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
    private val queryData: MutableLiveData<Resource<List<ProductsItem>>> = MutableLiveData()
    val query: LiveData<Resource<List<ProductsItem>>>
        get() = queryData
    private val offersData: MutableLiveData<Resource<List<ProductsItem>>> = MutableLiveData()
    val offers: LiveData<Resource<List<ProductsItem>>>
        get() = offersData
    private val searchData: MutableLiveData<Resource<List<ProductsItem>>> = MutableLiveData()
    val search: LiveData<Resource<List<ProductsItem>>>
        get() = searchData

    private val favouriteData: MutableLiveData<Resource<List<ProductsItem>>> = MutableLiveData()
    val favourite: LiveData<Resource<List<ProductsItem>>>
        get() = favouriteData
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
    fun getQueryData(
        query:String,
        categoryId:String,
    ) {
        productDataHelper.getQuery(
            query,
            categoryId,
            { success ->
                queryData.value = Resource.success(success)
            },
            { failure ->
                queryData.value = Resource.error(failure)
            })
    }
    fun getQueryDataOffers(
        query:String,
    ) {
        productDataHelper.getQueryOffers(
            query,
            { success ->
                offersData.value = Resource.success(success)
            },
            { failure ->
                offersData.value = Resource.error(failure)
            })
    }
    fun getSearchData(
        query:String,
    ) {
        productDataHelper.getSearchQuery(
            query,
            { success ->
                searchData.value = Resource.success(success)
            },
            { failure ->
                searchData.value = Resource.error(failure)
            })
    }
    fun getFavouriteData(
        prefs: SharedPreferences
    ) {
        productDataHelper.getFavouriteData(
            prefs,
            { success ->
                favouriteData.value = Resource.success(success)
            },
            { failure ->
                favouriteData.value = Resource.error(failure)
            })
    }
}