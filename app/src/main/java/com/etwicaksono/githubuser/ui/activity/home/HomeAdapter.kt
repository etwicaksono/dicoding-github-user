package com.etwicaksono.githubuser.ui.activity.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.etwicaksono.githubuser.R
import com.etwicaksono.githubuser.databinding.ItemRowUserBinding
import com.etwicaksono.githubuser.entity.UsersListItem
import com.etwicaksono.githubuser.ui.activity.detail_user.DetailUserActivity

class HomeAdapter : ListAdapter<UsersListItem, HomeAdapter.HomeViewHolder>(DIFF_CALLBACK) {

    class HomeViewHolder(private val binding: ItemRowUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun ImageView.loadImage(url: String?) {
            Glide.with(this.context)
                .load(url)
                .placeholder(R.drawable.default_image)
                .into(this)
        }

        fun bind(user: UsersListItem) {
            binding.apply {
                tvUsername.text = user.username
                ivUser.loadImage(user.avatar)
            }

            binding.itemRowUser.setOnClickListener {
                val detailIntent = Intent(it.context, DetailUserActivity::class.java)
                detailIntent.putExtra("user", user)
                it.context.startActivity(detailIntent)
            }
        }
    }


    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<UsersListItem> =
            object : DiffUtil.ItemCallback<UsersListItem>() {
                override fun areItemsTheSame(
                    oldItem: UsersListItem,
                    newItem: UsersListItem
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: UsersListItem,
                    newItem: UsersListItem
                ): Boolean {
                    return oldItem == newItem
                }

            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }
}