package com.etwicaksono.githubuser.paging

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.etwicaksono.githubuser.R
import com.etwicaksono.githubuser.databinding.ItemRowUserBinding
import com.etwicaksono.githubuser.entity.UsersListItem
import com.etwicaksono.githubuser.ui.activity.detail_user.DetailUserActivity

class UserListPagingAdapter :
    PagingDataAdapter<UsersListItem, UserListPagingAdapter.ViewHolder>(UserComparator) {
    object UserComparator : DiffUtil.ItemCallback<UsersListItem>() {
        override fun areItemsTheSame(oldItem: UsersListItem, newItem: UsersListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UsersListItem, newItem: UsersListItem): Boolean {
            return oldItem == newItem
        }

    }

    class ViewHolder(val view: ItemRowUserBinding) : RecyclerView.ViewHolder(view.root)

    fun ImageView.loadImage(url:String?){
        Glide.with(this.context)
                .load(url)
                .placeholder(R.drawable.default_image)
                .into(this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)!!
        holder.view.apply {
            tvUsername.text = user.username
            ivUser.loadImage(user.avatar)
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