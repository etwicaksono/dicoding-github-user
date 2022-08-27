package com.etwicaksono.githubuser.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.etwicaksono.githubuser.api.RetrofitService
import com.etwicaksono.githubuser.entity.UsersListItem
import com.etwicaksono.githubuser.paging.UserPagingSource

class UserRepository constructor(private val apiService:RetrofitService) {
    fun getUsersList():LiveData<PagingData<UsersListItem>>{
        return Pager(
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { UserPagingSource(apiService) }
        ).liveData
    }
}