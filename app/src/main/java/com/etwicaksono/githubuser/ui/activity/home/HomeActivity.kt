package com.etwicaksono.githubuser.ui.activity.home

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
import com.etwicaksono.githubuser.paging.UserLoadStateAdapter
import com.etwicaksono.githubuser.paging.UserPagerAdapter
import com.etwicaksono.githubuser.repository.UserRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val userPagerAdapter = UserPagerAdapter()
    private lateinit var viewModel: HomeViewModel
    private var firstLoading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService = RetrofitService.getInstance()
        val userRepository = UserRepository(this,apiService)

        binding.rvUsers.adapter =
            userPagerAdapter.withLoadStateFooter(UserLoadStateAdapter(userPagerAdapter::retry))
        binding.rvUsers.apply {
            val layoutManager = LinearLayoutManager(this@HomeActivity)
            this.layoutManager = layoutManager
            addItemDecoration(DividerItemDecoration(this@HomeActivity, layoutManager.orientation))
        }

        viewModel = ViewModelProvider(
            this,
            HomeViewModel.Factory(userRepository)
        )[HomeViewModel::class.java]

        viewModel.apply {
            errorMessage.observe(this@HomeActivity) {
                Toast.makeText(this@HomeActivity, it, Toast.LENGTH_SHORT).show()
            }

            hasInternet(this@HomeActivity).observe(this@HomeActivity) {
                if (it == false) {
                    Toast.makeText(this@HomeActivity, "Internet unavailable", Toast.LENGTH_LONG)
                        .show()
                    binding.progressBar.isVisible = false
                }
            }
        }

        userPagerAdapter.addLoadStateListener { loadState ->
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
                    binding.noDataAccepted.isVisible = userPagerAdapter.itemCount < 1
                }
            }
        }


        lifecycleScope.launch {
            viewModel.getUsersList().observe(this@HomeActivity) {
                it?.let { userPagerAdapter.submitData(lifecycle, it) }
            }

            userPagerAdapter.loadStateFlow.collectLatest {
                if (it.refresh is LoadState.NotLoading) {
                    binding.noDataAccepted.isVisible = userPagerAdapter.itemCount < 1
                }
            }
        }
    }
}