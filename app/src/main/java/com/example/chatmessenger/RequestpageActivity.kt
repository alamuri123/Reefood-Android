package com.example.chatmessenger



import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatmessenger.databinding.ActivityRequestpageBinding

class RequestpageActivity : AppCompatActivity() {

    // Declare the ViewBinding variable
    private lateinit var binding: ActivityRequestpageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the binding object
        binding = ActivityRequestpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listeners for the buttons
        binding.rinbox.setOnClickListener {
            Toast.makeText(this@RequestpageActivity, "Navigate to Request Inbox", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@RequestpageActivity, RequestinboxActivity::class.java))
            finish()
        }

        binding.ryourrequest.setOnClickListener {
            Toast.makeText(this@RequestpageActivity, "Navigate to Your Request", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@RequestpageActivity, RequestActivity::class.java))
            finish()
        }
    }
}