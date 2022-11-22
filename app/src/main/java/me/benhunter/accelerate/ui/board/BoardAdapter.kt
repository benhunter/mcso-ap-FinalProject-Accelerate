package me.benhunter.accelerate.ui.board

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.benhunter.accelerate.databinding.CategoryBinding
import me.benhunter.accelerate.model.Category

// Board holds a List of TaskList. TaskList holds a List of Tasks.
class BoardAdapter(private val layoutInflater: LayoutInflater) :
    ListAdapter<Category, BoardAdapter.ViewHolder>(Diff()) {

    inner class ViewHolder(val categoryBinding: CategoryBinding) :
        RecyclerView.ViewHolder(categoryBinding.root) {
        init {
            Log.d(
                javaClass.simpleName,
                "BoardAdapter.ViewHolder.init boardListBinding $categoryBinding"
            )
        }
    }

    class Diff : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            TODO("Not yet implemented")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val categoryBinding =
            CategoryBinding.inflate(layoutInflater, parent, false)
//            TaskListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        Log.d(
            javaClass.simpleName, "width ${parent.width}"
        )
        val taskListLayoutWidth = (parent.width * 0.8).toInt()
        categoryBinding.root.layoutParams =
            LinearLayoutCompat.LayoutParams(
                taskListLayoutWidth,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
//        taskListBinding.root.setPadding(100)

//        val taskListView = layoutInflater.inflate(R.layout.task_list, parent, false)
//        taskListView.layoutParams = LinearLayoutCompat.LayoutParams(parent.width)

        // TODO Set width by LayoutParams?
//        LayoutInflater.from(parent.context).inflate(R.layout.task_list, parent)
//        val layoutParams = LinearLayout.LayoutParams(width, height)
//        taskListBinding.layoutParams = layoutParams

        return ViewHolder(categoryBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val taskList = getItem(position)
        holder.categoryBinding.listNameTv.text = taskList.name

        // Setup the RecyclerView for each list of Tasks
        holder.categoryBinding.tasksRv.layoutManager =
            LinearLayoutManager(holder.categoryBinding.root.context)
        val taskListAdapter = TaskListAdapter()
        holder.categoryBinding.tasksRv.adapter = taskListAdapter
        taskListAdapter.submitList(taskList.tasks)

    }

}
