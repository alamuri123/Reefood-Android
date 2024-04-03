package com.example.chatmessenger




import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatmessenger.databinding.ActivityDonatepageBinding

class DonatepageActivity : AppCompatActivity() {

    // Declare the ViewBinding variable
    private lateinit var binding: ActivityDonatepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the binding object
        binding = ActivityDonatepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listeners for the buttons
        binding.yella1.setOnClickListener {
            Toast.makeText(
                this@DonatepageActivity,
                "Navigate to Donation Inbox",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this@DonatepageActivity, InboxdonationActivity::class.java))
            finish()
        }

        binding.yella2.setOnClickListener {
            Toast.makeText(
                this@DonatepageActivity,
                "Navigate to Your Donations",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this@DonatepageActivity, OfferActivity::class.java))
            finish()
        }

    }
}
