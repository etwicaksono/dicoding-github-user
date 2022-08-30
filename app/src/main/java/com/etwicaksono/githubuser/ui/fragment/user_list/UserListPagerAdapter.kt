package com.etwicaksono.githubuser.ui.fragment.user_list

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.etwicaksono.githubuser.R

class UserListPagerAdapter(context: Context, private val username:String):FragmentStateAdapter(
    FragmentActivity()) {

    val tabs= listOf(context.getString(R.string.follower),context.getString(R.string.following))

    override fun getItemCount(): Int =tabs.size

    override fun createFragment(position: Int): Fragment {
        return UserListFragment.newInstance(tabs[position],username)
    }
}