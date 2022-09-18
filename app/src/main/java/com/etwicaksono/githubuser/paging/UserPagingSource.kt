package com.etwicaksono.githubuser.paging

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.etwicaksono.githubuser.R
import com.etwicaksono.githubuser.api.RetrofitService
import com.etwicaksono.githubuser.entity.UsersListItem

class UserPagingSource(
    private val context: Context,
    private val apiService: RetrofitService,
    private val page: String,
    private val username: String
) :
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
            val response = when {
                page == context.getString(R.string.follower) && username != "" -> apiService.getUserFollowers(
                    username, position
                ).body()

                page == context.getString(R.string.following) && username != "" -> apiService.getUserFollowing(
                    username, position
                ).body()

                page == context.getString(R.string.search) -> apiService.searchUser(
                    username
                ).body()?.items

                else -> {
                    apiService.getUsersList(lastDataId).body()
                }
            }
            lastDataId = response?.last()?.id ?: 0
            LoadResult.Page(
                data = response!!,
                prevKey = if (position == 1) null else position.minus(1),
                nextKey = position.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}