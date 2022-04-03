package com.bizmiz.umidjonmarket111.auth.get_started.sms_verify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bizmiz.umidjonmarket111.Resource
import com.bizmiz.umidjonmarket111.models.UserData

class UserDataViewModel(private val userDataHelper: UserDataHelper) : ViewModel() {
    private val setUserData: MutableLiveData<Resource<String?>> = MutableLiveData()
    val userData: LiveData<Resource<String?>>
        get() = setUserData
    private val setPhoneNumber: MutableLiveData<Resource<String?>> = MutableLiveData()
    val phoneNumber: LiveData<Resource<String?>>
        get() = setPhoneNumber
    private val getUserDate: MutableLiveData<Resource<UserData?>> = MutableLiveData()
    val user: LiveData<Resource<UserData?>>
        get() = getUserDate
    private val updateUserData: MutableLiveData<Resource<String?>> = MutableLiveData()
    val updateUser: LiveData<Resource<String?>>
        get() = updateUserData
    private val checkUserRegistered: MutableLiveData<Resource<String?>> = MutableLiveData()
    val checkUser: LiveData<Resource<String?>>
        get() = checkUserRegistered
    fun setUserData(
        uid: String,
        userPhotoUrl: String,
        name: String,
        surname: String,
        dateBirthday: String,
        gender: String,
        location: String,
        phoneNumber: String,
    ) {
        setUserData.value = Resource.loading()
        userDataHelper.setUserData(
            uid,
            userPhotoUrl,
            name,
            surname,
            dateBirthday,
            gender,
            location,
            phoneNumber,
            { success ->
                setUserData.value = Resource.success(success)
            },
            { failure ->
                setUserData.value = Resource.error(failure)
            },
            { check ->
                setUserData.value = Resource.check(check)
            })
    }

    fun setPhoneNumber(
        uid: String,
        phoneNumber: String,
    ) {
        setUserData.value = Resource.loading()
        userDataHelper.setPhoneNumber(
            uid,
            phoneNumber,
            { success ->
                setUserData.value = Resource.success(success)
            },
            { failure ->
                setUserData.value = Resource.error(failure)
            },
            { check ->
                setUserData.value = Resource.check(check)
            })
    }

    fun getUserData(
        uid: String
    ) {
        getUserDate.value = Resource.loading()
        userDataHelper.getUserDataByUid(
            uid,
            { success ->
                getUserDate.value = Resource.success(success)
            },
            { failure ->
                getUserDate.value = Resource.error(failure)
            })
    }
    fun checkUserRegistered(
        phoneNumber: String
    ) {
        checkUserRegistered.value = Resource.loading()
        userDataHelper.checkUserRegistered(
            phoneNumber,
            { success ->
                checkUserRegistered.value = Resource.success(success)
            },
            { failure ->
                checkUserRegistered.value = Resource.error(failure)
            })
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
    ) {
        updateUserData.value = Resource.loading()
        userDataHelper.updateUserData(
            uid,
            userPhotoUrl,
            name,
            surname,
            dateBirthday,
            gender,
            location,
            phoneNumber,
            { success ->
                updateUserData.value = Resource.success(success)
            },
            { failure ->
                updateUserData.value = Resource.error(failure)
            },
            { check ->
                updateUserData.value = Resource.check(check)
            })
    }
}