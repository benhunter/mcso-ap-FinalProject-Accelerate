package me.benhunter.accelerate.ui.board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
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
        // Inflate the layout for this fragment
        _binding = FragmentBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        (requireActivity() as MainActivity).supportActionBar?.title = args.boardName


        val boardAdapter =
            BoardAdapter(layoutInflater, parentFragmentManager, boardViewModel, viewLifecycleOwner)
        binding.boardRecyclerView.adapter = boardAdapter

        boardViewModel.observeCategories().observe(viewLifecycleOwner) {
            Log.d(TAG, "boardViewModel.observeCategories().observe submitList")
            boardAdapter.submitList(it)
        }

        boardViewModel.observeBoard().observe(viewLifecycleOwner) {
            Log.d(TAG, "boardViewModel.observeBoard().observe fetchCategories")
            boardViewModel.fetchCategories()
        }

        binding.createCategoryFab.setOnClickListener(::onClickCreateCategory)

        boardViewModel.setBoard(args.boardFirestoreId)
    }

    private fun onClickCreateCategory(view: View) {
        Snackbar
            .make(view, "onClickCreateCategory createCategory FAB", Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()

        val createCategoryDialogFragment = CreateCategoryDialogFragment()
        createCategoryDialogFragment.show(parentFragmentManager, "create_category")
    }
}