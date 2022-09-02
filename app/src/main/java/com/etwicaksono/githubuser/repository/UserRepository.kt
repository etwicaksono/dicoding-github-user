package com.etwicaksono.githubuser.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.etwicaksono.githubuser.api.RetrofitService
import com.etwicaksono.githubuser.entity.UsersListItem
import com.etwicaksono.githubuser.paging.UserPagingSource

class UserRepository constructor(
    private val context: Context,
    private val apiService: RetrofitService
) {
    fun getUsersList(
        page: String = "home",
        username: String = ""
    ): LiveData<PagingData<UsersListItem>> {
        return Pager(
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { UserPagingSource(context, apiService, page, username) }
        ).liveData
    }
}