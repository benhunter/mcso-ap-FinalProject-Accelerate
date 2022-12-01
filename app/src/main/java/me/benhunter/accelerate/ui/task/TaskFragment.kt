package me.benhunter.accelerate.ui.task

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import me.benhunter.accelerate.databinding.FragmentTaskBinding

class TaskFragment : Fragment() {
    private val TAG = javaClass.simpleName
    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    private val args: TaskFragmentArgs by navArgs()

    private val taskViewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")

        // Inflate the layout for this fragment
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")

        taskViewModel.setTask(args.taskId)

        taskViewModel.observeTask().observe(viewLifecycleOwner) {
            if (it == null) {
                binding.taskNameEdittext.setText("")
            } else {
                binding.taskNameEdittext.setText(it.name)
            }
        }

        binding.taskSaveButton.setOnClickListener {
            taskViewModel.saveName(binding.taskNameEdittext.text.toString())
            findNavController().popBackStack()
        }

        binding.taskCancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.taskDeleteButton.setOnClickListener {
            taskViewModel.delete()
            findNavController().popBackStack()
        }
    }
}