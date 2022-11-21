package me.benhunter.accelerate.ui.board

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import me.benhunter.accelerate.R
import me.benhunter.accelerate.databinding.BoardBinding
import me.benhunter.accelerate.model.Board

class MyBoardsAdapter(val layoutInflater: LayoutInflater) :
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
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(oldItem: Board, newItem: Board): Boolean {
            TODO("Not yet implemented")
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
            holder.itemView.findNavController().navigate(R.id.nav_board)
        }
    }
}
