package com.etwicaksono.githubuser.ui.activity.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.etwicaksono.githubuser.entity.UsersListItem
import com.etwicaksono.githubuser.repository.UserRepository

class HomeViewModel(private val userRepository:UserRepository):ViewModel() {
    class Factory constructor(private val repository:UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
                HomeViewModel(this.repository) as T
            }else{
                throw IllegalArgumentException("ViewModel Not Found")
            }

        }
    }

    val errorMessage=MutableLiveData<String>()

    fun getUsersList():LiveData<PagingData<UsersListItem>>{
        return userRepository.getUsersList().cachedIn(viewModelScope)
    }

    fun hasInternet(context: Context): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        result.value = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return result
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return result
            when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> result.value =
                    true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> result.value =
                    true
                else -> result.value = false
            }
            return result
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return result
            result.value = networkInfo.isConnected
            return result
        }
    }
}