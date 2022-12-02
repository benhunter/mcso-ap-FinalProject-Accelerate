package me.benhunter.accelerate.task

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import me.benhunter.accelerate.board.BoardViewModel
import me.benhunter.accelerate.category.CategoryViewModel
import me.benhunter.accelerate.databinding.FragmentTaskBinding
import java.util.*


class TaskFragment : Fragment() {
    private val TAG = javaClass.simpleName
    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    private val args: me.benhunter.accelerate.task.TaskFragmentArgs by navArgs()

    private val taskViewModel: TaskViewModel by activityViewModels()
    private val categoryViewModel: CategoryViewModel by activityViewModels()
    private val boardViewModel: BoardViewModel by activityViewModels()

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
        categoryViewModel.setCategory(args.categoryId)

        taskViewModel.observeTask().observe(viewLifecycleOwner) {
            if (it == null) {
                binding.taskNameEdittext.setText("")
            } else {
                binding.taskNameEdittext.setText(it.name)
            }
        }

        binding.taskSaveButton.setOnClickListener {
            val categorySelectedPosition = binding.taskCategorySpinner.selectedItemPosition
            val categoryIdNullable =
                boardViewModel.observeCategories().value?.get(categorySelectedPosition)?.firestoreId
            categoryIdNullable?.let { categoryId ->
                taskViewModel.saveNameAndCategory(
                    binding.taskNameEdittext.text.toString(),
                    categoryId
                )
            }
            findNavController().popBackStack()
        }

        binding.taskCancelButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.taskDeleteButton.setOnClickListener {
            taskViewModel.delete()
            findNavController().popBackStack()
        }

        val adapter = ArrayAdapter<String>(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
        )

        binding.taskCategorySpinner.adapter = adapter

        boardViewModel.observeCategories().observe(viewLifecycleOwner) { categoryList ->
            val categoryNames = categoryList.map { it.name }
            adapter.clear()
            adapter.addAll(categoryNames)
            val categoryPosition = categoryList.find { it.firestoreId == args.categoryId }?.position
            if (categoryPosition != null) {
                binding.taskCategorySpinner.setSelection(categoryPosition)
            }
        }

        binding.taskSetDateButton.setOnClickListener {
            showDatePickerDialog()
        }

        binding.taskSetTimeButton.setOnClickListener{
            showTimePickerDialog()
        }

    }

    // Credit https://www.digitalocean.com/community/tutorials/android-date-time-picker-dialog
    @SuppressLint("SetTextI18n")
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this.requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                val day = dayOfMonth.toString().padStart(2, '0')
                val month = (monthOfYear + 1).toString().padStart(2, '0')
                binding.taskDueDateEdittext.setText("$year-$month-$day")
            },
            currentYear,
            currentMonth,
            currentDay
        )
        datePickerDialog.show()
    }

    // Credit https://www.digitalocean.com/community/tutorials/android-date-time-picker-dialog
    @SuppressLint("SetTextI18n")
    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this.requireContext(),
            { _, hour, minute ->
                val hourPadded = hour.toString().padStart(2, '0')
                val minutePadded = minute.toString().padStart(2, '0')
                binding.taskDueTimeEdittext.setText("$hourPadded:$minutePadded")
            },
            currentHour,
            currentMinute,
            true
        )
        timePickerDialog.show()
    }
}