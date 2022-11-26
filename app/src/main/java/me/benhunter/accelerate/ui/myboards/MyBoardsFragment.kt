package me.benhunter.accelerate.ui.myboards

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
import com.google.android.material.snackbar.Snackbar
import me.benhunter.accelerate.AuthInit
import me.benhunter.accelerate.MainViewModel
import me.benhunter.accelerate.databinding.FragmentMyBoardsBinding
import me.benhunter.accelerate.ui.board.MyBoardsAdapter

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

        val boardAdapter = MyBoardsAdapter(layoutInflater, ::navToBoard)
        binding.myBoardsRecyclerView.adapter = boardAdapter

        binding.createBoardFab.setOnClickListener {
            Snackbar
                .make(it, "MyBoardsFragment createBoard FAB", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
            myBoardsViewModel.createBoard()
        }

        myBoardsViewModel.observeMyBoards().observe(this.viewLifecycleOwner) {
            Log.d(TAG, "observeMyBoards")
            boardAdapter.submitList(it)
        }

        myBoardsViewModel.fetchMyBoards()

        Log.d(TAG, "auth ${mainViewModel.getUser()}")
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

    private fun navToBoard(board_id: String) {
        val action = MyBoardsFragmentDirections.actionMyBoardsToBoard(board_id)
        findNavController().navigate(action)
    }
}
