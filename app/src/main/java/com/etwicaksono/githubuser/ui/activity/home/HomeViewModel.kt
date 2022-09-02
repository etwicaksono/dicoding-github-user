package com.etwicaksono.githubuser.ui.activity.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.etwicaksono.githubuser.api.RetrofitService
import com.etwicaksono.githubuser.entity.UsersListItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _listUsers = MutableLiveData<List<UsersListItem>>()
    val listUsers = _listUsers
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage = _errorMessage

    init {
        getAllUsers()
    }

    fun getAllUsers() {
        _isLoading.value = true
        val api = RetrofitService.getInstance().getAllUser()
        api.enqueue(object : Callback<List<UsersListItem>> {
            override fun onResponse(
                call: Call<List<UsersListItem>>,
                response: Response<List<UsersListItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUsers.postValue(response.body())
                } else {
                    _errorMessage.postValue("getAllUsers failure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UsersListItem>>, t: Throwable) {
                _isLoading.postValue(false)
                _errorMessage.postValue("getAllUsers onFailure: ${t.message.toString()}")
            }

        })
    }

    fun searchUser(username: String) {
        _isLoading.value = true
        val client = RetrofitService.getInstance().searchUser(username)
        client.enqueue(object : Callback<List<UsersListItem>> {
            override fun onResponse(
                call: Call<List<UsersListItem>>,
                response: Response<List<UsersListItem>>,
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        _listUsers.postValue(it)
                    }
                } else {
                    _errorMessage.value = "searchUser onResponse failure: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<List<UsersListItem>>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "searchUser onFailure: ${t.message.toString()}"
            }

        })
    }


}