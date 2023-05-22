package com.waesh.f_task.view.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.waesh.f_task.R
import com.waesh.f_task.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)
        val currentUser = auth.currentUser
        currentUser?.let {
            //go to list fragment
            goToList()
        }

        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirmedPassword = binding.etConfirmPassword.text.toString()

            Log.i("kkkCat", "onViewCreated: $password >> $confirmedPassword")
            if (email.isEmailValid()) {
                if (password != confirmedPassword) {
                    Toast.makeText(requireActivity(), "Passwords did not match", Toast.LENGTH_SHORT)
                        .show()
                } else
                    registerUser(email, password)
            } else
                Toast.makeText(
                    requireActivity(),
                    "Please enter a valid email address.",
                    Toast.LENGTH_SHORT
                ).show()
        }

        binding.tvSignUp.setOnClickListener {
            //go to sign in
            navController.navigate(R.id.action_signUpFragment_to_signInFragment)
        }
    }

    private fun goToList() {
        navController.navigate(R.id.action_signUpFragment_to_listFragment)
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isComplete) {
                    // go to list fragment
                    goToList()
                } else {
                    Toast.makeText(
                        requireActivity(),
                        task.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun String.isEmailValid() =
        !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}