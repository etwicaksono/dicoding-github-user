package com.etwicaksono.githubuser.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.etwicaksono.githubuser.api.RetrofitService
import com.etwicaksono.githubuser.entity.UsersListItem

class UserPagingSource(private val apiService: RetrofitService) :
    PagingSource<Int, UsersListItem>() {

    private var lastDataId = 0

    override fun getRefreshKey(state: PagingState<Int, UsersListItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UsersListItem> {
        return try {
            val position = params.key ?: 1
            val response = apiService.getUsersList(lastDataId)
            lastDataId = response.body()?.last()?.id ?: 0
            LoadResult.Page(
                data = response.body()!!,
                prevKey = if (position == 1) null else position.minus(1),
                nextKey = position.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}