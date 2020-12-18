package com.kbds.kbidpassreader.presentation.logs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kbds.kbidpassreader.R
import com.kbds.kbidpassreader.databinding.LogItemBinding
import com.kbds.kbidpassreader.domain.model.log.LogEntity
import com.kbds.kbidpassreader.domain.model.log.LogType

class LogsAdapter(private val viewModel: LogsViewModel) :
    ListAdapter<LogEntity, LogsAdapter.LogViewHolder>(LogDiffCallback()){

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(viewModel, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        return LogViewHolder.from(parent)
    }

    class LogViewHolder private constructor(val binding: LogItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: LogsViewModel, log: LogEntity) {
            binding.viewModel = viewModel
            binding.log = log

            when(log.log_type) {
                LogType.SUCCESS -> { binding.logImageView.setBackgroundResource(R.drawable.ic_success) }
                LogType.ERROR -> { binding.logImageView.setBackgroundResource(R.drawable.ic_error) }
                LogType.NORMAL -> { binding.logImageView.setBackgroundResource(R.drawable.ic_normal) }
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): LogViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = LogItemBinding.inflate(inflater, parent, false)

                return LogViewHolder(binding)
            }
        }
    }
}

class LogDiffCallback : DiffUtil.ItemCallback<LogEntity>() {
    override fun areItemsTheSame(oldItem: LogEntity, newItem: LogEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LogEntity, newItem: LogEntity): Boolean {
        return oldItem == newItem
    }
}