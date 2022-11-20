package me.benhunter.accelerate.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.benhunter.accelerate.TaskList
import me.benhunter.accelerate.TaskListAdapter
import me.benhunter.accelerate.databinding.TaskListBinding

// Board holds a List of TaskList. TaskList holds a List of Tasks.
class BoardAdapter(private val layoutInflater: LayoutInflater) :
    ListAdapter<TaskList, BoardAdapter.ViewHolder>(Diff()) {
    inner class ViewHolder(val taskListBinding: TaskListBinding) :
        RecyclerView.ViewHolder(taskListBinding.root) {
        init {
            Log.d(
                javaClass.simpleName,
                "BoardAdapter.ViewHolder.init boardListBinding $taskListBinding"
            )
        }
    }

    class Diff : DiffUtil.ItemCallback<TaskList>() {
        override fun areItemsTheSame(oldItem: TaskList, newItem: TaskList): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(oldItem: TaskList, newItem: TaskList): Boolean {
            TODO("Not yet implemented")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val taskListBinding =
            TaskListBinding.inflate(layoutInflater, parent, false)
//            TaskListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        Log.d(
            javaClass.simpleName, "width ${parent.width}"
        )
        val taskListLayoutWidth = (parent.width * 0.8).toInt()
        taskListBinding.root.layoutParams =
            LinearLayoutCompat.LayoutParams(taskListLayoutWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
//        taskListBinding.root.setPadding(100)

//        val taskListView = layoutInflater.inflate(R.layout.task_list, parent, false)
//        taskListView.layoutParams = LinearLayoutCompat.LayoutParams(parent.width)

        // TODO Set width by LayoutParams?
//        LayoutInflater.from(parent.context).inflate(R.layout.task_list, parent)
//        val layoutParams = LinearLayout.LayoutParams(width, height)
//        taskListBinding.layoutParams = layoutParams

        return ViewHolder(taskListBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val taskList = getItem(position)
        holder.taskListBinding.listNameTv.text = taskList.name

        // Setup the RecyclerView for each list of Tasks
        holder.taskListBinding.tasksRv.layoutManager =
            LinearLayoutManager(holder.taskListBinding.root.context)
        val taskListAdapter = TaskListAdapter()
        holder.taskListBinding.tasksRv.adapter = taskListAdapter
        taskListAdapter.submitList(taskList.tasks)

    }

}
