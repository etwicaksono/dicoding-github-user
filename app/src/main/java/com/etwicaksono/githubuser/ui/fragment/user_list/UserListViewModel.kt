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
    val isLoading = MutableLiveData<Boolean>()
    val page = MutableLiveData("home")
    val username = MutableLiveData("")

    fun getUsersList(): LiveData<PagingData<UsersListItem>> {
        return Transformations.switchMap(page) { page ->
            userRepository.getUsersList(page, username.value.toString())
        }.cachedIn(viewModelScope)
    }
}