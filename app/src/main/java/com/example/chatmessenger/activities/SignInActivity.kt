@file:Suppress("DEPRECATION")

package com.example.chatmessenger.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.chatmessenger.HomeFragment2
import com.example.chatmessenger.MainActivity
import com.example.chatmessenger.MainActivity2
import com.example.chatmessenger.R
import com.example.chatmessenger.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class SignInActivity : AppCompatActivity() {

    lateinit var name: String
    lateinit var email: String
    lateinit var password: String
    lateinit private var fbauth: FirebaseAuth
    lateinit private var pds: ProgressDialog
    lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        fbauth = FirebaseAuth.getInstance()

        if (fbauth.currentUser != null) {
            startActivity(Intent(this, MainActivity2::class.java))
            finish()
        }

        pds = ProgressDialog(this)

        binding.signInTextToSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.loginButton.setOnClickListener {
            email = binding.loginetemail.text.toString()
            password = binding.loginetpassword.text.toString()

            if (binding.loginetemail.text.isEmpty() || binding.loginetpassword.text.isEmpty()) {
                Toast.makeText(this, "Enter Email and Password", Toast.LENGTH_SHORT).show()
            } else {
                signIn(password, email)
            }
        }
    }

    private fun signIn(password: String, email: String) {
        pds.show()
        pds.setMessage("Signing In")

        fbauth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            pds.dismiss()
            if (task.isSuccessful) {
                startActivity(Intent(this,MainActivity2::class.java))
                finish()


        } else {
                Toast.makeText(applicationContext, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            pds.dismiss()
            when (exception) {
                is FirebaseAuthInvalidCredentialsException -> {
                    Toast.makeText(applicationContext, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(applicationContext, "Auth Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        pds.dismiss()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        pds.dismiss()
    }
}
