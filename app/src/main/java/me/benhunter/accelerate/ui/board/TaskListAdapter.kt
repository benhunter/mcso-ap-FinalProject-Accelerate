package me.benhunter.accelerate.ui.board

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import me.benhunter.accelerate.databinding.TaskBinding
import me.benhunter.accelerate.model.Task

class TaskListAdapter(val navToTask: (String, String) -> Unit) : ListAdapter<Task, TaskListAdapter.ViewHolder>(Diff()) {

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
            return oldItem.firestoreId == newItem.firestoreId
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val taskBinding = TaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(taskBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = getItem(position)
        holder.taskBinding.taskTv.text = task.name
        holder.taskBinding.taskCard.setOnClickListener {
            Snackbar
                .make(it, "Clicked task ${task.name} position ${task.position}", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()

            navToTask(task.firestoreId, task.categoryId)
        }
    }
}
