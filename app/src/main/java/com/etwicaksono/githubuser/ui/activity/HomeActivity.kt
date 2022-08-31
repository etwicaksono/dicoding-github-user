package com.etwicaksono.githubuser.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.etwicaksono.githubuser.api.RetrofitService
import com.etwicaksono.githubuser.databinding.ActivityHomeBinding
import com.etwicaksono.githubuser.paging.UserLoadStatePagingAdapter
import com.etwicaksono.githubuser.paging.UserListPagingAdapter
import com.etwicaksono.githubuser.repository.UserRepository
import com.etwicaksono.githubuser.ui.fragment.user_list.UserListViewModel
import com.etwicaksono.githubuser.util.ConnectivityStatus
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val userListPagingAdapter = UserListPagingAdapter()
    private lateinit var viewModel: UserListViewModel
    private var firstLoading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkConnectivity()

        val apiService = RetrofitService.getInstance()
        val userRepository = UserRepository(this, apiService)

        binding.rvUsers.adapter =
            userListPagingAdapter.withLoadStateFooter(UserLoadStatePagingAdapter(userListPagingAdapter::retry))
        binding.rvUsers.apply {
            val layoutManager = LinearLayoutManager(this@HomeActivity)
            this.layoutManager = layoutManager
            addItemDecoration(DividerItemDecoration(this@HomeActivity, layoutManager.orientation))
        }

        viewModel = ViewModelProvider(
            this,
            UserListViewModel.Factory(userRepository)
        )[UserListViewModel::class.java]

        viewModel.apply {
            errorMessage.observe(this@HomeActivity) {
                Toast.makeText(this@HomeActivity, it, Toast.LENGTH_SHORT).show()
            }

            lifecycleScope.launch {
                getUsersList().observe(this@HomeActivity) {
                    it?.let { userListPagingAdapter.submitData(lifecycle, it) }
                }
            }
        }

        userListPagingAdapter.addLoadStateListener { loadState ->
            binding.progressBar.isVisible = firstLoading
            if (loadState.refresh is LoadState.NotLoading || loadState.append is LoadState.NotLoading) {
                firstLoading = false

                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                error?.let {
                    binding.noDataAccepted.isVisible = userListPagingAdapter.itemCount < 1
                }
            }
        }
    }

    private fun checkConnectivity() {
        val connectivity = ConnectivityStatus(this)
        connectivity.observe(this) { isConnected ->
            if (!isConnected) {
                Toast.makeText(this@HomeActivity, "Internet unavailable", Toast.LENGTH_LONG)
                    .show()
                binding.progressBar.isVisible = false
            }
        }
    }
}