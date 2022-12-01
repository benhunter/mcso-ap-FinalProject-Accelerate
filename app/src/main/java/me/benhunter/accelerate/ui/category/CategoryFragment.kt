package me.benhunter.accelerate.ui.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import me.benhunter.accelerate.databinding.FragmentCategoryBinding
import me.benhunter.accelerate.ui.task.TaskViewModel

class CategoryFragment : Fragment() {
    private val TAG = javaClass.simpleName

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private val args: CategoryFragmentArgs by navArgs()

    private val taskViewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")

        // Inflate the layout for this fragment
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }
}