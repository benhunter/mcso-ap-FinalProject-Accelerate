package me.benhunter.accelerate.ui.board

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import me.benhunter.accelerate.databinding.CategoryBinding
import me.benhunter.accelerate.model.Category

// Board holds a List of Category. Category holds a List of Task.
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

        Log.d(
            javaClass.simpleName, "width ${parent.width}"
        )
        val taskListLayoutWidth = (parent.width * 0.8).toInt()
        categoryBinding.root.layoutParams =
            LinearLayoutCompat.LayoutParams(
                taskListLayoutWidth,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )


        categoryBinding.addTaskButton.setOnClickListener(::onClickAddTaskButton)

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

    private fun onClickAddTaskButton(view: View) {
        Snackbar
            .make(view, "Add Task", Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }

}
