package com.example.chatmessenger



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatmessenger.activities.SignInActivity
import com.example.chatmessenger.databinding.ActivityFirstpageBinding

class FirstpageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstpageBinding // Assuming you have a binding class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.customerButton.setOnClickListener {
            Toast.makeText(this@FirstpageActivity, "Customer button clicked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@FirstpageActivity, SignInActivity::class.java))
            finish()
        }

        binding.adminButton.setOnClickListener {
            Toast.makeText(this@FirstpageActivity, "Admin button clicked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@FirstpageActivity, AdminloginActivity::class.java))
            finish()
        }
    }

    // Override the onBackPressed method to handle the back button
    override fun onBackPressed() {
        // Add your custom logic here if needed
        super.onBackPressed()
    }
}