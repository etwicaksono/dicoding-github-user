package com.etwicaksono.githubuser.ui.fragment.user_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.etwicaksono.githubuser.R
import com.etwicaksono.githubuser.api.RetrofitService
import com.etwicaksono.githubuser.databinding.FragmentUserListBinding
import com.etwicaksono.githubuser.paging.UserListPagingAdapter
import com.etwicaksono.githubuser.paging.UserLoadStatePagingAdapter
import com.etwicaksono.githubuser.repository.UserRepository
import kotlinx.coroutines.launch

private const val ARG_PAGE = "USER_LIST_TYPE"
private const val ARG_USERNAME = "USER_LIST_USERNAME"

class UserListFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding
    private var _viewModel: UserListViewModel? = null
    private val viewModel get() = _viewModel
    private val userListPagingAdapter = UserListPagingAdapter()

    private var page: String = "home"
    private var username: String? = null
    private var firstLoading = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            page = it.getString(ARG_PAGE).toString()
            username = it.getString(ARG_USERNAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val apiService = RetrofitService.getInstance()
        val userRepository = UserRepository(context, apiService)
        _viewModel = ViewModelProvider(
            this,
            UserListViewModel.Factory(userRepository)
        )[UserListViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)

        binding?.rvUsers?.apply {
            adapter =
                userListPagingAdapter.withLoadStateFooter(
                    UserLoadStatePagingAdapter(
                        userListPagingAdapter::retry
                    )
                )
            this.layoutManager = layoutManager
            addItemDecoration(itemDecoration)
        }

        viewModel?.apply {
            errorMessage.observe(viewLifecycleOwner) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }

            lifecycleScope.launch {
                getUsersList().observe(viewLifecycleOwner) { listUser ->
                        if (listUser != null) {
                            binding?.tvEmpty?.isVisible = false
                            userListPagingAdapter.submitData(lifecycle, listUser)
                        } else {
                            binding?.tvEmpty?.isVisible = true
                            binding?.tvEmpty?.text = when (page.value) {
                                context?.getString(R.string.follower) -> context?.getString(R.string.follower_not_found)
                                context?.getString(R.string.following) -> context?.getString(R.string.following_not_found)
                                else -> context?.getString(R.string.users_not_found)
                            }
                        }
                    }

            }
        }

        userListPagingAdapter.addLoadStateListener { loadState ->
            binding?.progressBar?.isVisible = firstLoading
            if (loadState.refresh is LoadState.NotLoading || loadState.append is LoadState.NotLoading) {
                firstLoading = false

                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                error.let {
                    binding?.tvEmpty?.isVisible =
                        userListPagingAdapter.itemCount < 1 && !firstLoading
                }
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(page: String, username: String) =
            UserListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PAGE, page)
                    putString(ARG_USERNAME, username)
                }
            }
    }
}