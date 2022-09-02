package com.etwicaksono.githubuser.ui.activity.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.etwicaksono.githubuser.R
import com.etwicaksono.githubuser.databinding.ActivityHomeBinding
import com.etwicaksono.githubuser.paging.UserListPagingAdapter
import com.etwicaksono.githubuser.util.ConnectivityStatus
import kotlinx.coroutines.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val userListPagingAdapter = UserListPagingAdapter()
    private val viewModel: HomeViewModel by viewModels()
    private var firstLoading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.page_title_home)
        checkConnectivity()

        val mHomeAdapter = HomeAdapter()
        binding.rvUsers.apply {
            val layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = mHomeAdapter
            this.layoutManager = layoutManager
            addItemDecoration(DividerItemDecoration(this@HomeActivity, layoutManager.orientation))
        }

        viewModel.apply {
            errorMessage.observe(this@HomeActivity) {
                Toast.makeText(this@HomeActivity, it, Toast.LENGTH_SHORT).show()
            }
            listUsers.observe(this@HomeActivity) { listUser ->
                if (listUser != null) {
                    mHomeAdapter.submitList(listUser)
                }
            }
            isLoading.observe(this@HomeActivity) { isLoading ->
                binding.progressBar.isVisible = isLoading
            }
        }

        val searchManager =
            this@HomeActivity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding.svUser.apply {
            setSearchableInfo(searchManager.getSearchableInfo(this@HomeActivity.componentName))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
                private var searchJob: Job? = null

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    searchJob?.cancel()
                    searchJob = coroutineScope.launch {
                        newText?.let { keyword ->
                            delay(500)
                            if (keyword.isEmpty()) {
//                                queryHint = context.getString(R.string.input_username)
//                                viewModel.getUsersList()
                            } else {
//                                queryHint = context.getString(R.string.search_user)
                                Toast.makeText(this@HomeActivity, "halo halo", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                    return false
                }

            })


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