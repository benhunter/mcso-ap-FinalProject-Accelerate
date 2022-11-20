package me.benhunter.accelerate.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import me.benhunter.accelerate.MainViewModel
import me.benhunter.accelerate.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val boardAdapter = BoardAdapter(layoutInflater)
        binding.boardRecyclerView.adapter = boardAdapter

        Log.d(javaClass.simpleName, "board ${homeViewModel.board}")
        boardAdapter.submitList(homeViewModel.board)

        Log.d(javaClass.simpleName, "auth ${mainViewModel.updateUser()}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}