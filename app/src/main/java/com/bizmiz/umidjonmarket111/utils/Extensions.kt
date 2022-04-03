package com.bizmiz.umidjonmarket111.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import com.bizmiz.umidjonmarket111.R
fun View.hideKeyboard() {
    val inputMethodManager: InputMethodManager? =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)
}
fun View.showSoftKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}
fun twoCheckedRadioButton(rdb1: RadioButton, rdb2: RadioButton) {
    rdb1.isChecked = true
    rdb2.isChecked = false
}
val listCategoryImg: ArrayList<Int> = arrayListOf(
    R.drawable.cat1,
    R.drawable.cat2,
    R.drawable.cat3,
    R.drawable.cat4,
    R.drawable.cat5,
    R.drawable.cat6,
    R.drawable.cat7,
    R.drawable.cat8,
    R.drawable.cat1,
    R.drawable.cat2,
    R.drawable.cat3,
    R.drawable.cat4,
    R.drawable.cat5,
    R.drawable.cat6,
    R.drawable.cat7,
    R.drawable.cat8
)

val listExclusiveImg: ArrayList<Int> = arrayListOf(
    R.drawable.exclusive1,
    R.drawable.exclusive2,
    R.drawable.exclusive3,
    R.drawable.exclusive4,
    R.drawable.exclusive1,
    R.drawable.exclusive2,
    R.drawable.exclusive3,
    R.drawable.exclusive4
)
val listCategoryName: ArrayList<String> = arrayListOf(
    "Sabzavotlar",
    "Sut va tuxum maxsulotlari",
    "Shirinliklar",
    "Ichimliklar",
    "Mevalar",
    "Go'sht maxsulotlari",
    "Sharbatlar",
    "Non maxsulotlari",
    "Sabzavotlar",
    "Sut va tuxum maxsulotlari",
    "Shirinliklar",
    "Ichimliklar",
    "Mevalar",
    "Go'sht maxsulotlari",
    "Sharbatlar",
    "Non maxsulotlari"
)
val listExclusiveName: ArrayList<String> = arrayListOf(
    "Qulupnay",
    "Anor",
    "Ananas",
    "Apelsin",
    "Qulupnay",
    "Anor",
    "Ananas",
    "Apelsin"
)