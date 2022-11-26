package me.benhunter.accelerate.ui.board

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import me.benhunter.accelerate.databinding.BoardBinding
import me.benhunter.accelerate.model.Board

class MyBoardsAdapter(
    val layoutInflater: LayoutInflater,
    val navToBoard: (board_id: String) -> Unit
) :
    ListAdapter<Board, MyBoardsAdapter.ViewHolder>(Diff()) {

    inner class ViewHolder(val boardBinding: BoardBinding) :
        RecyclerView.ViewHolder(boardBinding.root) {
        init {
            Log.d(
                javaClass.simpleName,
                "MyBoardsAdapter.ViewHolder.init boardBinding $boardBinding"
            )
        }
    }

    class Diff : DiffUtil.ItemCallback<Board>() {
        override fun areItemsTheSame(oldItem: Board, newItem: Board): Boolean {
            return oldItem.firestoreId == newItem.firestoreId
        }

        override fun areContentsTheSame(oldItem: Board, newItem: Board): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBoardsAdapter.ViewHolder {
        val boardBinding = BoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(boardBinding)
    }

    override fun onBindViewHolder(holder: MyBoardsAdapter.ViewHolder, position: Int) {
        val board = getItem(position)
        holder.boardBinding.boardNameTv.text = board.name
        holder.boardBinding.boardNameCard.setOnClickListener {
            val text = "Clicked board ${board.name}"
            Snackbar
                .make(holder.itemView, text, Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()

            navToBoard(board.name)
        }
    }
}
