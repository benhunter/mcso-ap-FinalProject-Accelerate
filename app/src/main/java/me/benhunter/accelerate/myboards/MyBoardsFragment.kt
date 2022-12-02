package me.benhunter.accelerate.myboards

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import me.benhunter.accelerate.AuthInit
import me.benhunter.accelerate.MainViewModel
import me.benhunter.accelerate.board.CreateBoardDialogFragment
import me.benhunter.accelerate.databinding.FragmentMyBoardsBinding

class MyBoardsFragment : Fragment() {

    private val TAG = javaClass.simpleName
    private var _binding: FragmentMyBoardsBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val myBoardsViewModel: MyBoardsViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyBoardsBinding.inflate(inflater, container, false)

        // Firebase Auth
        // TODO do before other setup?
        AuthInit(mainViewModel, signInLauncher)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myBoardsAdapter = MyBoardsAdapter(layoutInflater, ::navToBoard)
        binding.myBoardsRecyclerView.adapter = myBoardsAdapter

        binding.createBoardFab.setOnClickListener(::onClickCreateBoard)

        myBoardsViewModel.observeMyBoards().observe(this.viewLifecycleOwner) {
            Log.d(TAG, "observeMyBoards")
            myBoardsAdapter.submitList(it)

            // show "create a board" prompt when empty
            if (it.isEmpty()) {
                binding.myBoardsRecyclerView.visibility = View.INVISIBLE
                binding.myBoardsPromptToCreateCardview.visibility = View.VISIBLE
            } else {
                binding.myBoardsRecyclerView.visibility = View.VISIBLE
                binding.myBoardsPromptToCreateCardview.visibility = View.INVISIBLE
            }
        }

        myBoardsViewModel.fetchMyBoards()

        Log.d(TAG, "auth ${mainViewModel.getUser()}")

        binding.myBoardsSwiperefresh.setOnRefreshListener {
            myBoardsViewModel.fetchMyBoards()
            binding.myBoardsSwiperefresh.isRefreshing = false
        }
    }

    private fun onClickCreateBoard(view: View) {
        val createBoardDialogFragment = CreateBoardDialogFragment()
        createBoardDialogFragment.show(parentFragmentManager, "create_board")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            mainViewModel.updateUser()
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            Log.d(TAG, "sign in failed $result")
        }
    }

    private fun navToBoard(boardFirestoreId: String, board_name: String) {
        val action =
            MyBoardsFragmentDirections.actionMyBoardsToBoard(
                boardFirestoreId,
                board_name
            )
        findNavController().navigate(action)
    }
}
