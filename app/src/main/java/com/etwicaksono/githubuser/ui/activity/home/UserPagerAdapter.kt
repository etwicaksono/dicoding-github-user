package com.etwicaksono.githubuser.ui.activity.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.etwicaksono.githubuser.R
import com.etwicaksono.githubuser.databinding.ItemRowUserBinding
import com.etwicaksono.githubuser.entity.UsersListItem
import com.etwicaksono.githubuser.ui.activity.DetailUserActivity

class UserPagerAdapter :
    PagingDataAdapter<UsersListItem, UserPagerAdapter.ViewHolder>(UserComparator) {
    object UserComparator : DiffUtil.ItemCallback<UsersListItem>() {
        override fun areItemsTheSame(oldItem: UsersListItem, newItem: UsersListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UsersListItem, newItem: UsersListItem): Boolean {
            return oldItem == newItem
        }

    }

    class ViewHolder(val view: ItemRowUserBinding) : RecyclerView.ViewHolder(view.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)!!
        holder.view.apply {
            tvUsername.text = user.username
            Glide.with(ivUser.context)
                .load(user.avatar)
                .placeholder(R.drawable.default_image)
                .into(ivUser)
        }

        holder.view.itemRowUser.setOnClickListener {
            val detailIntent = Intent(it.context, DetailUserActivity::class.java)
            detailIntent.putExtra("user", user)
            it.context.startActivity(detailIntent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRowUserBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

}