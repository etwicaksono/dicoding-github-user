package com.etwicaksono.githubuser.ui.activity.detail_user

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.etwicaksono.githubuser.R
import com.etwicaksono.githubuser.databinding.ActivityDetailUserBinding
import com.etwicaksono.githubuser.entity.UsersListItem
import com.etwicaksono.githubuser.paging.UserPagerAdapter
import com.etwicaksono.githubuser.ui.fragment.user_list.UserListPagerAdapter
import com.etwicaksono.githubuser.ui.fragment.user_list.UserListViewModel
import com.google.android.material.tabs.TabLayoutMediator
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val detailUserViewModel: DetailUserViewModel by viewModels()
    private var position = 0
    private lateinit var titles: List<String>
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userIntent = intent.getParcelableExtra<UsersListItem>("user")
        val actionBar = supportActionBar
        username = userIntent?.username.toString()

        actionBar?.apply {
            "@".also { this.title = it + username }
            setDisplayHomeAsUpEnabled(true)
        }

        detailUserViewModel.apply {
            if (userIntent != null) {
                getUserData(userIntent.username)
            }

            isLoading.observe(this@DetailUserActivity) { isLoading ->
                binding.progressBar.isVisible = isLoading
            }

            userData.observe(this@DetailUserActivity) {
                binding.apply {
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatarUrl)
                        .placeholder(R.drawable.default_image)
                        .into(ivUser)

                    tvName.text = it.name
                    tvRepository.text = numberFormat(it.publicRepos.toString())
                    tvFollower.text = numberFormat(it.followers.toString())
                    tvFollowing.text = numberFormat(it.following.toString())
                    it.htmlUrl.let {
                        tvHtmlUrl.text = it
                        tvHtmlUrl.isVisible = !it.isNullOrEmpty()
                    }
                    it.company.let {
                        tvCompany.text = it
                        tvCompany.isVisible = !it.isNullOrEmpty()
                    }
                    it.location.let {
                        tvLocation.text = it
                        tvLocation.isVisible = !it.isNullOrEmpty()
                    }
                }

            }

        }

        val sectionPagerAdapter =
            UserListPagerAdapter(this@DetailUserActivity, username)

        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            titles = sectionPagerAdapter.tabs
            this.position = position
            tab.text = titles[position]
        }.attach()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun numberFormat(value: String, useSymbol: Boolean = false): String {
        val result: Any
        if (useSymbol) {
            val local = Locale("id", "ID")
            val cursIndo = NumberFormat.getCurrencyInstance(local) as DecimalFormat
            val symbol = Currency.getInstance(local).getSymbol(local)
            cursIndo.positivePrefix = "$symbol. "
            result = cursIndo.format(value.toDouble())

        } else {
            result = NumberFormat.getInstance().format(value.toDouble())
        }

        return result.toString()

    }
}