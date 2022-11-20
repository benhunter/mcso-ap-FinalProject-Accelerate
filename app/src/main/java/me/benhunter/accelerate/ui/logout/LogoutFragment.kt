package me.benhunter.accelerate.ui.logout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import me.benhunter.accelerate.MainViewModel
import me.benhunter.accelerate.R
import me.benhunter.accelerate.databinding.FragmentLogoutBinding

class LogoutFragment : Fragment() {
    private val viewModel: MainViewModel by viewModels()
    private var _binding: FragmentLogoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogoutBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logoutNoButtton.setOnClickListener {
            findNavController().navigate(R.id.nav_home)

            // TODO remove
            val toastText = "No"
            Toast
                .makeText(context, toastText, Toast.LENGTH_LONG)
                .show()
        }

        binding.logoutYesButtton.setOnClickListener{

            // TODO remove
            val toastText = "Logging out ${viewModel.getUser()?.email}"
            Toast
                .makeText(context, toastText, Toast.LENGTH_LONG)
                .show()

            viewModel.signOut()
            findNavController().navigate(R.id.nav_home)
        }
    }
}