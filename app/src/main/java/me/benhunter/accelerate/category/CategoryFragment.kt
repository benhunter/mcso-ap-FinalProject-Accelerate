package me.benhunter.accelerate.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import me.benhunter.accelerate.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment() {
    private val TAG = javaClass.simpleName

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private val args: CategoryFragmentArgs by navArgs()

    private val categoryViewModel: CategoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")

        // Inflate the layout for this fragment
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")

        categoryViewModel.setCategory(args.taskId)

        categoryViewModel.observeCategory().observe(viewLifecycleOwner) {
            if (it == null) {
                binding.categoryNameEdittext.setText("")
            } else {
                binding.categoryNameEdittext.setText(it.name)
            }
        }

        binding.categorySaveButton.setOnClickListener {
            categoryViewModel.saveName(binding.categoryNameEdittext.text.toString())
            findNavController().popBackStack()
        }

        binding.categoryCancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.categoryDeleteButton.setOnClickListener {
            categoryViewModel.delete()
            findNavController().popBackStack()
        }
    }
}