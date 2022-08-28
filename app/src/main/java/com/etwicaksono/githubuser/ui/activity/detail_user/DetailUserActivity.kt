package com.etwicaksono.githubuser.ui.activity.detail_user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.etwicaksono.githubuser.R
import com.etwicaksono.githubuser.databinding.ActivityDetailUserBinding
import com.etwicaksono.githubuser.entity.UsersListItem
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val viewModel: DetailUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        val userIntent = intent.getParcelableExtra<UsersListItem>("user")

        actionBar?.apply {
            "@".also { this.title = it + userIntent?.username }
            setDisplayHomeAsUpEnabled(true)
        }
        viewModel.apply {
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