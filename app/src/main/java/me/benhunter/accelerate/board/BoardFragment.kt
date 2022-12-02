package me.benhunter.accelerate.board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import me.benhunter.accelerate.MainActivity
import me.benhunter.accelerate.databinding.FragmentBoardBinding

class BoardFragment : Fragment() {

    private val TAG = javaClass.simpleName
    private var _binding: FragmentBoardBinding? = null
    private val binding get() = _binding!!

    private val args: BoardFragmentArgs by navArgs()

    private val boardViewModel: BoardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")

        // Inflate the layout for this fragment
        _binding = FragmentBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")

        boardViewModel.setBoard(args.boardFirestoreId)

        (requireActivity() as MainActivity).supportActionBar?.title = args.boardName

        val boardAdapter =
            BoardAdapter(
                layoutInflater,
                parentFragmentManager,
                boardViewModel,
                viewLifecycleOwner,
                ::navToTask,
                ::navToCategory
            )
        binding.boardRecyclerView.adapter = boardAdapter

        boardViewModel.observeBoard().observe(viewLifecycleOwner) {
            Log.d(TAG, "boardViewModel.observeBoard().observe fetchCategories")
            boardViewModel.fetchCategories()
            boardViewModel.fetchTasks()
        }

        boardViewModel.observeCategories().observe(viewLifecycleOwner) {
            Log.d(TAG, "boardViewModel.observeCategories().observe submitList")

            // Prefer to filter in BoardViewModel
            val categoriesForCurrentBoard = it.filter { it.boardId == args.boardFirestoreId }
            boardAdapter.submitList(categoriesForCurrentBoard)

            // show "create a category" prompt when empty
            if (it.isEmpty()){
                binding.boardRecyclerView.visibility = View.INVISIBLE
                binding.boardsPromptToCreateCardview.visibility = View.VISIBLE
            } else {
                binding.boardRecyclerView.visibility = View.VISIBLE
                binding.boardsPromptToCreateCardview.visibility = View.INVISIBLE
            }
        }

        binding.createCategoryFab.setOnClickListener(::onClickCreateCategory)
        binding.shareBoardFab.setOnClickListener(::onClickShareBoard)

        binding.boardSwiperefresh.setOnRefreshListener {
            boardViewModel.fetchBoardCategoriesAndTasks()
            binding.boardSwiperefresh.isRefreshing = false
        }
    }

    private fun onClickCreateCategory(view: View) {
        val createCategoryDialogFragment = CreateCategoryDialogFragment()
        createCategoryDialogFragment.show(parentFragmentManager, "create_category")
    }

    private fun onClickShareBoard(view: View) {
        val shareBoardDialogFragment = ShareBoardDialogFragment()
        shareBoardDialogFragment.show(parentFragmentManager, "share_board")
    }

    private fun navToTask(taskId: String, categoryId: String) {
        val action = BoardFragmentDirections.actionBoardToTask(
            taskId,
            categoryId
        )
        findNavController().navigate(action)
    }

    private fun navToCategory(categoryId: String) {
        val action = BoardFragmentDirections.actionBoardToCategory(
            categoryId
        )
        findNavController().navigate(action)
    }
}