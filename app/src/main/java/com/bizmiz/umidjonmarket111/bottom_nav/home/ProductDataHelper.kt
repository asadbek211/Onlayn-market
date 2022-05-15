package com.bizmiz.umidjonmarket111.bottom_nav.home

import android.content.SharedPreferences
import com.bizmiz.umidjonmarket111.models.ProductsItem
import com.bizmiz.umidjonmarket111.utils.Constant
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ProductDataHelper(private val db: FirebaseFirestore) {
    fun getProduct(
        onSuccess: (products: List<ProductsItem>) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val list: ArrayList<ProductsItem> = arrayListOf()
        db.collection(Constant.PRODUCTS).orderBy("create_data", Query.Direction.DESCENDING)
            .get().addOnSuccessListener {
                it.documents.forEach { data ->
                    val model = data.toObject(ProductsItem::class.java)
                    if (model != null
                    ) {
                        list.add(model)
                    }
                }
                onSuccess.invoke(list)
            }.addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }

    fun getProductByCategoryId(
        categoryId: String,
        productId: String,
        onSuccess: (products: List<ProductsItem>) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val list: ArrayList<ProductsItem> = arrayListOf()
        db.collection(Constant.PRODUCTS).orderBy("create_data", Query.Direction.DESCENDING)
            .get().addOnSuccessListener {
                it.documents.forEach { data ->
                    val model = data.toObject(ProductsItem::class.java)
                    if (model != null && model.categoryId == categoryId && model.id != productId
                    ) {
                        list.add(model)
                    }
                }
                onSuccess.invoke(list)
            }.addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }

    fun getQuery(
        query: String,
        categoryId: String,
        onSuccess: (products: List<ProductsItem>) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val list: java.util.ArrayList<ProductsItem> = arrayListOf()
        db.collection(Constant.PRODUCTS).orderBy("create_data", Query.Direction.DESCENDING)
            .get().addOnSuccessListener {
                it.documents.forEach { data ->
                    val model = data.toObject(ProductsItem::class.java)
                    if (model != null && model.categoryId == categoryId && model.name.lowercase().contains(query.lowercase())
                    ) {
                        list.add(model)
                    }
                }
                onSuccess.invoke(list)
            }.addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
    fun getSearchQuery(
        query: String,
        onSuccess: (products: List<ProductsItem>) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val list: ArrayList<ProductsItem> = arrayListOf()
        db.collection(Constant.PRODUCTS).orderBy("create_data", Query.Direction.DESCENDING)
            .get().addOnSuccessListener {
                it.documents.forEach { data ->
                    val model = data.toObject(ProductsItem::class.java)
                    if (model != null && model.name.lowercase().contains(query.lowercase())
                    ) {
                        list.add(model)
                    }
                }
                onSuccess.invoke(list)
            }.addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
    fun getQueryOffers(
        query: String,
        onSuccess: (products: List<ProductsItem>) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val listQuery: ArrayList<ProductsItem> = arrayListOf()
        val list: ArrayList<ProductsItem> = arrayListOf()
        db.collection(Constant.PRODUCTS).orderBy("create_data", Query.Direction.DESCENDING)
            .get().addOnSuccessListener {
                it.documents.forEach { data ->
                    val model = data.toObject(ProductsItem::class.java)
                    if (model != null && model.endPrice != ""
                    ) {
                        if (query.isNotEmpty() && model.name.lowercase().contains(query.lowercase())) {
                            listQuery.add(model)
                        } else {
                            list.add(model)
                        }
                    }
                }
                if (query.isNotEmpty()) {
                    onSuccess.invoke(listQuery)
                } else {
                    onSuccess.invoke(list)
                }
            }.addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
    fun getFavouriteData(
        prefs: SharedPreferences,
        onSuccess: (list: ArrayList<ProductsItem>) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val list: ArrayList<ProductsItem> = arrayListOf()
        db.collection(Constant.PRODUCTS)
            .orderBy("create_data", Query.Direction.DESCENDING).get()
            .addOnSuccessListener {
                it.documents.forEach { doc ->
                    val model = doc.toObject(ProductsItem::class.java)
                    if (model != null && prefs.contains(model.id)) list.add(
                        model
                    )
                }
                onSuccess.invoke(list)
            }.addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
}
