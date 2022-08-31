package com.etwicaksono.githubuser.ui.activity.detail_user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.etwicaksono.githubuser.api.RetrofitService
import com.etwicaksono.githubuser.entity.UserDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _userData = MutableLiveData<UserDetail>()
    val userData: LiveData<UserDetail> = _userData

    fun getUserData(username: String) {
        val api = RetrofitService.getInstance()
        val client = api.getUserDetail(username)
        client.enqueue(object : Callback<UserDetail> {
            override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                if (response.isSuccessful) {
                    _userData.postValue(response.body())
                } else {
                    val message = "getUserData onResponse failure: ${response.message()}"
                    Log.e(this@DetailUserViewModel::class.java.simpleName, message)
                    _errorMessage.value = message
                }
            }

            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                val message = "getUserData onFailure: ${t.message.toString()}"
                Log.e(this@DetailUserViewModel::class.java.simpleName, message)
                _errorMessage.value = message
            }
        })
    }
}