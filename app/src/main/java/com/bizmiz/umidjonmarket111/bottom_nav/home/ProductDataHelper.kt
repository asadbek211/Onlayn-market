package com.bizmiz.umidjonmarket111.bottom_nav.home

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
}
