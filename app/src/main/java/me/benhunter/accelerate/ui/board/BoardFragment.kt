package me.benhunter.accelerate.ui.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import me.benhunter.accelerate.MainActivity
import me.benhunter.accelerate.databinding.FragmentBoardBinding
import me.benhunter.accelerate.ui.myboards.MyBoardsViewModel

class BoardFragment : Fragment() {

    private val TAG = javaClass.simpleName
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
        (requireActivity() as MainActivity).supportActionBar?.title = args.boardName

        val boardAdapter = BoardAdapter(layoutInflater, parentFragmentManager)
        binding.boardRecyclerView.adapter = boardAdapter

//        myBoardsViewModel.observeCurrentBoardCategories().observe(viewLifecycleOwner){
//            boardAdapter.submitList(it)
//        }

        myBoardsViewModel.observeCurrentBoard().observe(viewLifecycleOwner) {
            boardAdapter.submitList(it.categories)
        }

//        boardAdapter.submitList()

        binding.createCategoryFab.setOnClickListener(::onClickCreateCategory)
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