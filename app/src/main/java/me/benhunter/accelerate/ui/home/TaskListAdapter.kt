package me.benhunter.accelerate.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.benhunter.accelerate.databinding.TaskBinding
import me.benhunter.accelerate.model.Task

class TaskListAdapter : ListAdapter<Task, TaskListAdapter.ViewHolder>(Diff()) {

    inner class ViewHolder(val taskBinding: TaskBinding) :
        RecyclerView.ViewHolder(taskBinding.root) {
        init {
            Log.d(
                javaClass.simpleName,
                "TaskListAdapter.ViewHolder.init $taskBinding"
            )
        }
    }

    class Diff : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            TODO("Not yet implemented")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val taskBinding = TaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)

//        taskBinding.taskLinearLayout.minimumWidth = parent.width

//        val layoutParams = LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        return ViewHolder(taskBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = getItem(position)

        holder.taskBinding.taskTv.text = task.name
    }
}
