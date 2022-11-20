package me.benhunter.accelerate.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import me.benhunter.accelerate.BoardList
import me.benhunter.accelerate.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

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

        binding.boardRecyclerView.layoutManager =
            LinearLayoutManager(binding.boardRecyclerView.context)

        binding.boardRecyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)

        val boardAdapter = BoardAdapter()
        binding.boardRecyclerView.adapter = boardAdapter

        val categories = List(5) {
            BoardList("category $it")
        }
        Log.d(javaClass.simpleName, "categories $categories")
        boardAdapter.submitList(categories)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}