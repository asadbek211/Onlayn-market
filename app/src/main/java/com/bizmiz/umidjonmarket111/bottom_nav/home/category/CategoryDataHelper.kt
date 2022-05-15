package com.bizmiz.umidjonmarket111.bottom_nav.home.category

import com.bizmiz.umidjonmarket111.utils.Constant
import com.bizmiz.umidjonmarket111.models.CategoryItem
import com.bizmiz.umidjonmarket111.models.ProductsItem
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.ArrayList

class CategoryDataHelper(private val db: FirebaseFirestore) {
    fun getCategory(
        onSuccess: (user: List<CategoryItem>) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val list: ArrayList<CategoryItem> = arrayListOf()
        db.collection(Constant.CATEGORY)
            .get().addOnSuccessListener {
                it.documents.forEach { data ->
                    val model = data.toObject(CategoryItem::class.java)
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
}