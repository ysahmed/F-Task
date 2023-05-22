package com.waesh.f_task.view.fragment


import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.waesh.f_task.R
import com.waesh.f_task.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        navController = Navigation.findNavController(view)
        val currentUser = auth.currentUser
        //android.util.Log.i("kkkCat", "sign In: $currentUser?.uid")

        if (currentUser != null){
            navController.navigate(R.id.action_signInFragment_to_listFragment)
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            if (email.isEmailValid())
                signIn(email, password)
            else
                Toast.makeText(requireActivity(), "Invalid email format!", Toast.LENGTH_SHORT).show()
        }

        binding.tvSignUp.setOnClickListener {
            //go to sign up
            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //go to list fragment
                    navController.navigate(R.id.action_signInFragment_to_listFragment)
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