package com.example.chatmessenger

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatmessenger.databinding.ActivityAdminHomeBinding
class AdminHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.donateImageView.setOnClickListener {

            startActivity(Intent(this@AdminHomeActivity, AdminofferActivity::class.java))
            finish()
        }
        binding.requestImageView.setOnClickListener {

            startActivity(Intent(this@AdminHomeActivity, AdminrequestActivity::class.java))
            finish()
        }
        binding.logout.setOnClickListener {
            Toast.makeText(this@AdminHomeActivity, "Admin logged out", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@AdminHomeActivity, FirstpageActivity::class.java))
            finish()
        }

        // Handle clicks or perform any initialization here
    }


    override fun onBackPressed() {
        val intent = Intent(this, AdminloginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
