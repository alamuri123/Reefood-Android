package com.example.chatmessenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.chatmessenger.databinding.NavHeaderBinding
import com.example.chatmessenger.fragments.SettingFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

// Import your ViewModel here
import com.example.chatmessenger.mvvm.ChatAppViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.razorpay.PaymentResultListener

class MainActivity2 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,PaymentResultListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var viewModel: ChatAppViewModel

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Initialize ViewModel using ViewModelProvider
        viewModel = ViewModelProvider(this).get(ChatAppViewModel::class.java)

        // Bind ViewModel to nav_header
        val navHeaderBinding = NavHeaderBinding.bind(navigationView.getHeaderView(0))
        navHeaderBinding.lifecycleOwner = this
        navHeaderBinding.viewModel = viewModel

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment2())
            navigationView.setCheckedItem(R.id.nav_home)
        }

        // Load image into ImageView
        val navHeaderImageView = navHeaderBinding.settingUpdateImage
        viewModel.imageUrl.observe(this, { imageUrl ->
            Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.ic_baseline_person_24) // Placeholder image while loading
                .error(R.drawable.ic_launcher_foreground) // Error image if loading fails
                .into(navHeaderImageView)
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> replaceFragment(HomeFragment2())
            R.id.nav_profile -> replaceFragment(SettingFragment())
            // Add more cases for other navigation items
            R.id.nav_chats -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
//            R.id.nav_chats -> replaceFragment(HomeFragment())
            R.id.nav_logout -> {
                val firebaseAuth = FirebaseAuth.getInstance()
                firebaseAuth.signOut()
                startActivity(Intent(this, FirstpageActivity::class.java))
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show()
        // Retrieve the amount value from SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val amount = sharedPreferences.getString("AMOUNT", "") // Default value 0.0f if key not found


        // Here, you can store the payment ID and amount to the database
        // For simplicity, let's just display the payment ID
        amount.let {
            savePaymentToDatabase(razorpayPaymentId ?: "", amount)
        }
    }

    override fun onPaymentError(response: Int, p1: String?) {
        // Payment failure logic
        Toast.makeText(this, "Payment Failed: $response", Toast.LENGTH_SHORT).show()
    }

    private fun savePaymentToDatabase(paymentId: String, amount: String?) {
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            val paymentRef = firebaseDatabase.getReference("payments").child(userId).child(paymentId)
            val totalAmountRef = firebaseDatabase.getReference("TotalAmount")
            val paymentData = Payment(paymentId, amount.toString())

            totalAmountRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val currentTotalAmount = dataSnapshot.getValue(Double::class.java) ?: 0.0

                    // Update the total amount with the new payment amount
                    val newTotalAmount = currentTotalAmount + (amount?.toDoubleOrNull() ?: 0.0)

                    // Save the updated total amount back to the database
                    totalAmountRef.setValue(newTotalAmount)
                        .addOnSuccessListener {
                            Log.d("MainActivity2", "Total amount updated successfully: $newTotalAmount")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("MainActivity2", "Failed to update total amount", exception)
                        }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                    Log.e("MainActivity2", "Database error: ${databaseError.message}")
                }
            })

            paymentRef.setValue(paymentData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Payment details saved successfully", Toast.LENGTH_SHORT).show()
                    Log.d("MainActivity2", "Payment details saved successfully: $paymentData")
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to save payment details", Toast.LENGTH_SHORT).show()
                    Log.e("MainActivity2", "Failed to save payment details", exception)
                }
        } else {
            Log.e("HomeFragment2", "User ID is null")
        }

    }
}