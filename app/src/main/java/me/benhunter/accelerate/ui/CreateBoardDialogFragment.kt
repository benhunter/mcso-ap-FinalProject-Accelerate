package me.benhunter.accelerate.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import me.benhunter.accelerate.R
import me.benhunter.accelerate.databinding.FragmentCreateBoardDialogBinding
import me.benhunter.accelerate.ui.myboards.MyBoardsViewModel

class CreateBoardDialogFragment : DialogFragment() {

    private val myBoardsViewModel: MyBoardsViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val inflater = requireActivity().layoutInflater

//            val view = inflater.inflate(R.layout.fragment_create_board_dialog, null)
            val binding = FragmentCreateBoardDialogBinding.inflate(inflater)
            val view = binding.root

            val builder = AlertDialog.Builder(it)

            val alertDialog =
            builder.setMessage(R.string.create_a_board)

                .setPositiveButton(
                    R.string.create
                ) { dialog, id -> //
                    // TODO get text for name
//                        R.id.create_board_edittext
                    val name = binding.createBoardNameEdittext.text.toString()
                    if (name.isNotEmpty()){

                    myBoardsViewModel.createBoard(name)
                    } else {
                        val text = "Please put a name"
                        Toast
                            .makeText(context, text, Toast.LENGTH_LONG)
                            .show()
                        dialog.cancel()
                    }
                }

                .setNegativeButton(
                    R.string.cancel
                ) { dialog, id ->
                    dismiss()
                }

                .setView(view)
                .create()

            binding.createBoardNameEdittext.addTextChangedListener {
                val alertDialogWhenNameTextChanged = dialog as AlertDialog
                alertDialogWhenNameTextChanged.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = it.toString().isNotEmpty()
            }

            return alertDialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onResume() {
        super.onResume()

        val alertDialog = dialog as AlertDialog
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
    }
}