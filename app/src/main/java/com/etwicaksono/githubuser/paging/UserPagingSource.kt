package com.etwicaksono.githubuser.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.etwicaksono.githubuser.api.RetrofitService
import com.etwicaksono.githubuser.entity.UsersListItem

class UserPagingSource(private val apiService: RetrofitService) :
    PagingSource<Int, UsersListItem>() {
    override fun getRefreshKey(state: PagingState<Int, UsersListItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UsersListItem> {
        return try {
            val position = params.key ?: 0
            val response = apiService.getUsersList(position)
            LoadResult.Page(
                data = response.body()!!,
                prevKey = if (position == 0) null else position.minus(1),
                nextKey = position.plus(1)
            )
        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }

}