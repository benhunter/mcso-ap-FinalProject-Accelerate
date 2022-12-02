package me.benhunter.accelerate.board

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import me.benhunter.accelerate.R
import me.benhunter.accelerate.databinding.FragmentShareBoardDialogBinding

class ShareBoardDialogFragment : DialogFragment() {

    private val boardViewModel: BoardViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val inflater = requireActivity().layoutInflater
            val binding = FragmentShareBoardDialogBinding.inflate(inflater)
            val view = binding.root
            val builder = AlertDialog.Builder(it)

            val alertDialog =
                builder.setMessage("Share this board")

                    .setPositiveButton(
                        R.string.share
                    ) { dialog, id ->
                        val email = binding.shareBoardEmailEdittext.text.toString()
                        if (email.isNotEmpty()) {
                            boardViewModel.shareBoard(email)
                        } else {
                            val text = "Please put an email"
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

            binding.shareBoardEmailEdittext.addTextChangedListener {
                val alertDialogWhenNameTextChanged = dialog as AlertDialog
                alertDialogWhenNameTextChanged.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                    it.toString().isNotEmpty()
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
