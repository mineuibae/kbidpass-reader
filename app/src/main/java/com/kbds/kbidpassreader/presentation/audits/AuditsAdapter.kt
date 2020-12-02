package com.kbds.kbidpassreader.presentation.audits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kbds.kbidpassreader.R
import com.kbds.kbidpassreader.databinding.AuditItemBinding
import com.kbds.kbidpassreader.domain.model.Audit
import com.kbds.kbidpassreader.domain.model.AuditType

class AuditsAdapter(private val viewModel: AuditsViewModel) :
    ListAdapter<Audit, AuditsAdapter.AuditViewHolder>(AuditDiffCallback()){

    override fun onBindViewHolder(holder: AuditViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(viewModel, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuditViewHolder {
        return AuditViewHolder.from(parent)
    }

    class AuditViewHolder private constructor(val binding: AuditItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: AuditsViewModel, audit: Audit) {
            binding.viewModel = viewModel
            binding.audit = audit

            when(audit.audit_type) {
                AuditType.SUCCESS -> { binding.auditImageView.setBackgroundResource(R.drawable.ic_success) }
                AuditType.ERROR -> { binding.auditImageView.setBackgroundResource(R.drawable.ic_error) }
                AuditType.NORMAL -> { binding.auditImageView.setBackgroundResource(R.drawable.ic_normal) }
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AuditViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = AuditItemBinding.inflate(inflater, parent, false)

                return AuditViewHolder(binding)
            }
        }
    }
}

class AuditDiffCallback : DiffUtil.ItemCallback<Audit>() {
    override fun areItemsTheSame(oldItem: Audit, newItem: Audit): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Audit, newItem: Audit): Boolean {
        return oldItem == newItem
    }
}