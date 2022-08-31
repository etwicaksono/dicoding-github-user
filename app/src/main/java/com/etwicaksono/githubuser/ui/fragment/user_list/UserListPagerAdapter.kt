package com.etwicaksono.githubuser.ui.fragment.user_list

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.etwicaksono.githubuser.R

class UserListPagerAdapter(activity: AppCompatActivity, private val username: String) :
    FragmentStateAdapter(activity) {

    val tabs = listOf(activity.getString(R.string.follower), activity.getString(R.string.following))

    override fun getItemCount(): Int = tabs.size

    override fun createFragment(position: Int): Fragment {
        return UserListFragment.newInstance(tabs[position], username)
    }
}