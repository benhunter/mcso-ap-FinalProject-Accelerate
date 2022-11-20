package me.benhunter.accelerate.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.benhunter.accelerate.BoardList
import me.benhunter.accelerate.databinding.BoardListBinding

class BoardAdapter : ListAdapter<BoardList, BoardAdapter.ViewHolder>(Diff()) {
    inner class ViewHolder(val boardListBinding: BoardListBinding) : RecyclerView.ViewHolder(boardListBinding.root){
//        lateinit var boardList: BoardList
        init {
            Log.d(javaClass.simpleName, "BoardAdapter.ViewHolder.init boardListBinding $boardListBinding")
        }
    }

    class Diff : DiffUtil.ItemCallback<BoardList>() {
        override fun areItemsTheSame(oldItem: BoardList, newItem: BoardList): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(oldItem: BoardList, newItem: BoardList): Boolean {
            TODO("Not yet implemented")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val boardListBinding = BoardListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(boardListBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val boardList = getItem(position)
        holder.boardListBinding.listNameTv.text = boardList.name
    }

}
