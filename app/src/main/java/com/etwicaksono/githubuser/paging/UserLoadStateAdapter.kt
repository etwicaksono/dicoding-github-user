package com.etwicaksono.githubuser.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.etwicaksono.githubuser.R
import com.etwicaksono.githubuser.databinding.ItemRowLoadingBinding

class UserLoadStateAdapter(private val retry:()->Unit):LoadStateAdapter<UserLoadStateAdapter.LoadStateViewHolder>() {
    class LoadStateViewHolder(parent:ViewGroup,retry: () -> Unit):RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(
        R.layout.item_row_loading,parent,false)) {

        private val binding = ItemRowLoadingBinding.bind(itemView)
        private val progressBar = binding.progressBar
        private val message = binding.progressMessage
        private val retry = binding.btnRetry.also { it.setOnClickListener { retry() } }

        fun bind(loadState: LoadState){
            if(loadState is LoadState.Error){
                message.text=loadState.error.localizedMessage
            }

            progressBar.isVisible=loadState is LoadState.Loading
            retry.isVisible=loadState is LoadState.Error
            message.isVisible=loadState is LoadState.Error
        }

    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        return holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(parent,retry)
    }
}