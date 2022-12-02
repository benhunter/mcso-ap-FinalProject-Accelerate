package me.benhunter.accelerate.board

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.ItemTouchHelper.*
import com.google.android.material.snackbar.Snackbar
import me.benhunter.accelerate.databinding.CategoryBinding
import me.benhunter.accelerate.model.Category
import me.benhunter.accelerate.model.Task

// Board holds a List of Category. Category holds a List of Task.
class BoardAdapter(
    private val layoutInflater: LayoutInflater,
    private val fragmentManager: FragmentManager,
    private val boardViewModel: BoardViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
    private val navToTask: (String, String) -> Unit,
    val navToCategory: (String) -> Unit,
) :
    ListAdapter<Category, BoardAdapter.ViewHolder>(Diff()) {

    private val TAG = javaClass.simpleName

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
            return oldItem.firestoreId == newItem.firestoreId
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
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

        return ViewHolder(categoryBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = getItem(position)
        holder.categoryBinding.listNameTv.text = category.name

        // Setup the RecyclerView for each list of Tasks
        holder.categoryBinding.tasksRv.layoutManager =
            LinearLayoutManager(holder.categoryBinding.root.context)
        val taskListAdapter = TaskListAdapter(navToTask)
        initTouchHelper(category, taskListAdapter, holder).attachToRecyclerView(holder.categoryBinding.tasksRv)
        holder.categoryBinding.tasksRv.adapter = taskListAdapter


        boardViewModel.observeTasks().observe(viewLifecycleOwner) { tasks ->
            Log.d(TAG, "boardViewModel.observeTasks().observe category.name=${category.name}")
            val tasksFilteredByBoardAndSorted =
                tasks
                    .filter { it.categoryId == category.firestoreId }
                    .sortedBy { task: Task -> task.position }
            taskListAdapter.submitList(tasksFilteredByBoardAndSorted)
        }

        holder.categoryBinding.addTaskButton.setOnClickListener {
            Snackbar
                .make(it, "Add Task to ${category.name}", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()

            val createTaskDialogFragment = CreateTaskDialogFragment(category)
            createTaskDialogFragment.show(fragmentManager, "create_task")
        }

        holder.categoryBinding.listNameTv.setOnClickListener {
            Snackbar
                .make(it, "Clicked category ${category.name} position ${category.position}", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
            navToCategory(category.firestoreId)
        }
    }

    private fun initTouchHelper(
        category: Category,
        taskListAdapter: TaskListAdapter,
        holder: ViewHolder
    ): ItemTouchHelper {
        val simpleItemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(UP or DOWN or START or END, ItemTouchHelper.START)
            {
                override fun onMove(recyclerView: RecyclerView,
                                    viewHolder: RecyclerView.ViewHolder,
                                    target: RecyclerView.ViewHolder): Boolean {
                    // TODO
                    Snackbar
                        .make(holder.itemView, "ItemTouchHelp onMove", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show()

                    val from = viewHolder.adapterPosition
                    val to = target.adapterPosition

                    boardViewModel.moveTask(from, to, category.firestoreId)
                    // TODO may need adapter.moveItem? should be from observer!

                    taskListAdapter.notifyItemMoved(from, to)

                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    return
                }
            }
        return ItemTouchHelper(simpleItemTouchCallback)
    }

}
