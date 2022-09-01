package com.etwicaksono.githubuser.ui.fragment.user_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import com.etwicaksono.githubuser.entity.UsersListItem
import com.etwicaksono.githubuser.repository.UserRepository

class UserListViewModel(private val userRepository: UserRepository) : ViewModel() {
    class Factory constructor(private val repository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(UserListViewModel::class.java)) {
                UserListViewModel(this.repository) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }

    val errorMessage = MutableLiveData<String>()
    private val _userList = MutableLiveData<PagingData<UsersListItem>>()
    val userList: LiveData<PagingData<UsersListItem>> = _userList

    fun getUsersList(
        page: String = "home",
        username: String = ""
    ) {
        _userList.postValue(userRepository.getUsersList(page, username).value)
    }

    fun searchUser(keyword: String) {
        _userList.postValue(userRepository.searchUser(keyword).value)
    }
}