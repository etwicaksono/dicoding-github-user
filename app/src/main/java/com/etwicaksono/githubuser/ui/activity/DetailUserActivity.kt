package com.etwicaksono.githubuser.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.etwicaksono.githubuser.R
import com.etwicaksono.githubuser.databinding.ActivityDetailUserBinding
import com.etwicaksono.githubuser.entity.UsersListItem

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userIntent = intent.getParcelableExtra<UsersListItem>("user")

        binding.apply {
            Glide.with(this@DetailUserActivity)
                .load(userIntent?.avatar)
                .placeholder(R.drawable.default_image)
                .into(ivUser)
        }
    }
}