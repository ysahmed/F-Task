package com.waesh.f_task.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.waesh.f_task.databinding.TaskItemBinding
import com.waesh.f_task.model.Task

class TasksAdapter(private val itemClickListener: ItemClickListener): ListAdapter<Task, TasksAdapter.TaskViewHolder>(Comparator()) {

    class TaskViewHolder(private val binding: TaskItemBinding) : ViewHolder(binding.root) {
        fun bind(task: Task){
            binding.apply {
                tvTask.text = task.value
            }
        }
    }

    class Comparator: DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem.key == newItem.key

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = TaskViewHolder(binding)

        binding.ivEdit.setOnClickListener {
            itemClickListener.onClickEdit(currentList[holder.adapterPosition])
        }

        binding.ivDelete.setOnClickListener {
            itemClickListener.onClickDelete(currentList[holder.adapterPosition])
        }

        return holder
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

interface ItemClickListener {
    fun onClickEdit(task: Task)
    fun onClickDelete(task: Task)
}

