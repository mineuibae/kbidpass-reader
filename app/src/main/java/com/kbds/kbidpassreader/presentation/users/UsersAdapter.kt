package com.kbds.kbidpassreader.presentation.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kbds.kbidpassreader.R
import com.kbds.kbidpassreader.databinding.UserItemBinding
import com.kbds.kbidpassreader.domain.model.User

class UsersAdapter(private val viewModel: UsersViewModel) :
    ListAdapter<User, UsersAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(viewModel, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder.from(parent)
    }

    class UserViewHolder private constructor(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: UsersViewModel, user: User) {
            binding.viewModel = viewModel
            binding.user = user
            if(user.is_registered) {
                binding.userImageView.setBackgroundResource(R.drawable.ic_user_registered)
            } else {
                binding.userImageView.setBackgroundResource(R.drawable.ic_user_unregistered)
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): UserViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = UserItemBinding.inflate(inflater, parent, false)

                return UserViewHolder(binding)
            }
        }
    }
}

class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}