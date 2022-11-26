package me.benhunter.accelerate.ui.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import me.benhunter.accelerate.MainActivity
import me.benhunter.accelerate.databinding.FragmentBoardBinding
import me.benhunter.accelerate.ui.myboards.MyBoardsViewModel

class BoardFragment : Fragment() {

    private var _binding: FragmentBoardBinding? = null
    private val binding get() = _binding!!

    private val args: BoardFragmentArgs by navArgs()

    private val myBoardsViewModel: MyBoardsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // TODO remove
//        binding.fragmentBoardNameTv.text = args.boardId
        (requireActivity() as MainActivity).supportActionBar?.title = args.boardId

        val boardAdapter = BoardAdapter(layoutInflater)
        binding.boardRecyclerView.adapter = boardAdapter
        val board = myBoardsViewModel.getBoardById(args.boardId)
        boardAdapter.submitList(board?.categories)
    }
}