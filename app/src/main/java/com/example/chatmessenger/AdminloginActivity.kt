package com.example.chatmessenger



import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatmessenger.databinding.ActivityAdminloginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminloginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminloginBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminloginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("admin")

        binding.loginButton.setOnClickListener {
            val loginEmail = binding.loginemail.text.toString()
            val loginPassword = binding.loginpassword.text.toString()

            if (loginEmail.isNotEmpty() && loginPassword.isNotEmpty()) {
                loginUser(loginEmail, loginPassword)
            } else {
                Toast.makeText(
                    this@AdminloginActivity,
                    "All Fields are required",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }

    private fun loginUser(email: String, password: String) {
        databaseReference.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val adminData = userSnapshot.getValue(AdminData::class.java)

                            if (adminData != null && adminData.password == password) {
                                Toast.makeText(
                                    this@AdminloginActivity,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent =
                                    Intent(this@AdminloginActivity, AdminHomeActivity::class.java)
                                startActivity(intent)  // Start AdminHomeActivity
                                finish()
                                return
                            }
                        }
                        // Password does not match any user
                        Toast.makeText(
                            this@AdminloginActivity,
                            "Login Failed: Incorrect password",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Email not found
                        Toast.makeText(
                            this@AdminloginActivity,
                            "Login Failed: User not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(
                        this@AdminloginActivity,
                        "Database Error: ${databaseError.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    // Override the onBackPressed method to handle the back button
    override fun onBackPressed() {
        val intent = Intent(this, FirstpageActivity::class.java)
        startActivity(intent)
        finish()
    }
}
