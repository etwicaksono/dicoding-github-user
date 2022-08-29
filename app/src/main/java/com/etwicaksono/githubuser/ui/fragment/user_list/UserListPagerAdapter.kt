package com.etwicaksono.githubuser.ui.fragment.user_list

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.etwicaksono.githubuser.R

class UserListPagerAdapter(fm: Fragment, private val username:String):FragmentStateAdapter(fm) {

    private val tabs= listOf(fm.getString(R.string.follower),fm.getString(R.string.following))

    override fun getItemCount(): Int =tabs.size

    override fun createFragment(position: Int): Fragment {
        return UserListFragment.newInstance(tabs[position],username)
    }
}