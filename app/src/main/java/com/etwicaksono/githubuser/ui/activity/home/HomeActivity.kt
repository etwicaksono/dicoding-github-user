package com.etwicaksono.githubuser.ui.activity.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.etwicaksono.githubuser.R
import com.etwicaksono.githubuser.api.RetrofitService
import com.etwicaksono.githubuser.databinding.ActivityHomeBinding
import com.etwicaksono.githubuser.repository.UserRepository
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
        val userRepository = UserRepository(apiService)

        binding.rvUsers.adapter = userPagerAdapter
        binding.rvUsers.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProvider(
            this,
            HomeViewModel.Factory(userRepository)
        )[HomeViewModel::class.java]

        viewModel.apply {
            errorMessage.observe(this@HomeActivity){
                Toast.makeText(this@HomeActivity,it,Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launch{
            viewModel.getUsersList().observe(this@HomeActivity){
                it?.let { userPagerAdapter.submitData(lifecycle,it) }
            }
        }
    }
}