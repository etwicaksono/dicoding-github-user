package com.etwicaksono.githubuser.ui.fragment.user_list

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
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

    fun hasInternet(context: Context): LiveData<Boolean> {
        val isConnected = MutableLiveData<Boolean>()
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isConnected.postValue(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                isConnected.postValue(false)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                isConnected.postValue(false)
            }
        }
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && connectivityManager.activeNetwork == null) {
            isConnected.postValue(false)
        }

        connectivityManager.registerNetworkCallback(networkRequest,networkCallback)

        return isConnected
    }
}