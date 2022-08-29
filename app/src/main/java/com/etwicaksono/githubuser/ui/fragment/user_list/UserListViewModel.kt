package com.etwicaksono.githubuser.ui.fragment.user_list

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.ContextCompat.getSystemService
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

    fun hasInternet(): LiveData<Boolean> {
        val isConnected = MutableLiveData<Boolean>()

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isConnected.value = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                isConnected.value = false
            }
        }

        val connectivityManager= getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest,networkCallback)

        return isConnected
    }
}