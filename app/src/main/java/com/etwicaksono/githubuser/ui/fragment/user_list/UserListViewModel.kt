package com.etwicaksono.githubuser.ui.fragment.user_list

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
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

    fun getUsersList(
        page: String = "home",
        username: String = ""
    ): LiveData<PagingData<UsersListItem>> {
        return userRepository.getUsersList(page, username).cachedIn(viewModelScope)
    }

    fun searchUser(keyword:String): LiveData<PagingData<UsersListItem>> {
        return userRepository.searchUser(keyword).cachedIn(viewModelScope)
    }
}