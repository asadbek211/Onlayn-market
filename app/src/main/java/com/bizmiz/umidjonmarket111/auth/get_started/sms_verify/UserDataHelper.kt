package com.bizmiz.umidjonmarket111.auth.get_started.sms_verify

import com.bizmiz.umidjonmarket111.utils.Constant
import com.bizmiz.umidjonmarket111.models.UserData
import com.google.firebase.firestore.FirebaseFirestore

class UserDataHelper(private val db: FirebaseFirestore) {
    fun getUserDataByUid(
        uid: String,
        onSuccess: (user: UserData) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        db.collection(Constant.USER_COLLECTION).document(uid)
            .get().addOnSuccessListener {
                val model = it.toObject(UserData::class.java)
                if (model != null) {
                    onSuccess.invoke(model)
                }

            }.addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
    fun checkUserRegistered(
        phoneNumber: String,
        onSuccess: (isRegistered: String) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val list: ArrayList<String> = arrayListOf()
        db.collection(Constant.USER_COLLECTION)
            .get().addOnSuccessListener {
                it.documents.forEach { doc ->
                    val model = doc.toObject(UserData::class.java)
                    if (model != null){
                        list.add(model.phoneNumber)
                    }
                }
                if (list.contains(phoneNumber)){
                    onSuccess.invoke("registered")
                }else{
                    onSuccess.invoke("unregistered")
                }
            }.addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
    fun setUserData(
        uid: String,
        userPhotoUrl: String,
        name: String,
        surname: String,
        dateBirthday: String,
        gender: String,
        location: String,
        phoneNumber: String,
        onSuccess: (success: String?) -> Unit,
        onFailure: (msg: String?) -> Unit,
        check: (msg: String?) -> Unit
    ) {
        val map: MutableMap<String, Any?> = mutableMapOf()
        map["uid"] = uid
        map["userPhotoUrl"] = userPhotoUrl
        map["name"] = name
        map["surname"] = surname
        map["dateBirthday"] = dateBirthday
        map["gender"] = gender
        map["location"] = location
        map["phoneNumber"] = phoneNumber
        db.collection(Constant.USER_COLLECTION).document(uid).set(map)
            .addOnSuccessListener {
                onSuccess.invoke("success")
            }
            .addOnFailureListener { e ->
                onFailure.invoke(e.localizedMessage)
            }
        check.invoke("check")
    }

    fun setPhoneNumber(
        uid: String,
        phoneNumber: String,
        onSuccess: (success: String?) -> Unit,
        onFailure: (msg: String?) -> Unit,
        check: (msg: String?) -> Unit
    ) {
        db.collection(Constant.USER_COLLECTION).document(uid).update("phoneNumber", phoneNumber)
            .addOnSuccessListener {
                onSuccess.invoke("update")
            }
            .addOnFailureListener { e ->
                onFailure.invoke(e.localizedMessage)
            }
        check.invoke("check")
    }

    fun updateUserData(
        uid: String,
        userPhotoUrl: String,
        name: String,
        surname: String,
        dateBirthday: String,
        gender: String,
        location: String,
        phoneNumber: String,
        onSuccess: (success: String?) -> Unit,
        onFailure: (msg: String?) -> Unit,
        check: (msg: String?) -> Unit
    ) {
        val map: MutableMap<String, Any?> = mutableMapOf()
        if (uid.isNotEmpty()) {
            map["uid"] = uid
        }
        if (userPhotoUrl != "noUpdate"){
            map["userPhotoUrl"] = userPhotoUrl
        }
        if (name.isNotEmpty()) {
            map["name"] = name
        }
        if (surname.isNotEmpty()) {
            map["surname"] = surname
        }
        if (dateBirthday.isNotEmpty()) {
            map["dateBirthday"] = dateBirthday
        }
        if (gender.isNotEmpty()) {
            map["gender"] = gender
        }
        if (location.isNotEmpty()) {
            map["location"] = location
        }
        if (phoneNumber.isNotEmpty()) {
            map["phoneNumber"] = phoneNumber
        }
        db.collection(Constant.USER_COLLECTION).document(uid).update(map)
            .addOnSuccessListener {
                onSuccess.invoke("success")
            }
            .addOnFailureListener { e ->
                onFailure.invoke(e.localizedMessage)
            }
        check.invoke("check")
    }

}