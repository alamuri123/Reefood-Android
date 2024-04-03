package com.example.chatmessenger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.chatmessenger.databinding.FragmentHome2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject




//class HomeFragment2 : Fragment(), PaymentResultListener {
//
//    private var _binding: FragmentHome2Binding? = null
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentHome2Binding.inflate(inflater, container, false)
//
//        // Set click listeners for the image views
//        binding.donateImageView.setOnClickListener {
//            Toast.makeText(requireContext(), "Donate Clicked", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(requireContext(), DonatepageActivity::class.java))
//        }
//        binding.requestImageView.setOnClickListener {
//            Toast.makeText(requireContext(), "Request Clicked", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(requireContext(), RequestpageActivity::class.java))
//        }
//        binding.aboutImageView.setOnClickListener {
//            Toast.makeText(requireContext(), "About Clicked", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(requireContext(), about::class.java))
//        }
//
//        // Set click listener for Donate Now button
//        binding.donateNowButton.setOnClickListener {
//            val amount = binding.amountEditText.text.toString().toDoubleOrNull()
//            val amountString = binding.amountEditText.text.toString()
//            val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//            val editor = sharedPreferences.edit()
//
//            editor.putString("AMOUNT", amountString) // You can use putDouble for API level 29 and above
//            editor.apply()
//            if (amount != null) {
//                initiatePayment(amount)
//            } else {
//                Toast.makeText(requireContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        return binding.root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    private fun initiatePayment(amount: Double) {
//        val checkout = Checkout()
//        checkout.setKeyID("rzp_test_j3JubQCOxSzYx8")
//
//        try {
//            val options = JSONObject()
//            options.put("name", "Reefood")
//            options.put("description", "Donation")
//            options.put("currency", "INR")
//            options.put("amount", (amount * 100).toInt())
//            val prefill = JSONObject()
//            prefill.put("email", "test@razorpay.com")
//            prefill.put("contact", "9999999999")
//            options.put("prefill", prefill)
//
//            checkout.open(requireActivity(), options)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    override fun onPaymentSuccess(razorpayPaymentId: String?) {
//        // Payment success logic
//        Toast.makeText(requireContext(), "Payment Successful", Toast.LENGTH_SHORT).show()
//
//        // Save payment details to Firebase Realtime Database
//        razorpayPaymentId?.let { paymentId ->
//            val amount = binding.amountEditText.text.toString().toDoubleOrNull()
//
//        }
//    }
//
//    override fun onPaymentError(code: Int, response: String?) {
//        // Payment failure logic
//        Toast.makeText(requireContext(), "Payment Failed: $response", Toast.LENGTH_SHORT).show()
//        // Additional logic if needed
//    }
//}

import com.google.firebase.database.*

class HomeFragment2 : Fragment(), PaymentResultListener {

    private var _binding: FragmentHome2Binding? = null
    private val binding get() = _binding!!

    // Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var totalAmountRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHome2Binding.inflate(inflater, container, false)

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        totalAmountRef = firebaseDatabase.getReference("TotalAmount")

        // Fetch total amount from the database
        fetchTotalAmount()

        // Set click listeners for the image views
        binding.donateImageView.setOnClickListener {
            Toast.makeText(requireContext(), "Donate Clicked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), DonatepageActivity::class.java))
        }
        binding.requestImageView.setOnClickListener {
            Toast.makeText(requireContext(), "Request Clicked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), RequestpageActivity::class.java))
        }
        binding.aboutImageView.setOnClickListener {
            Toast.makeText(requireContext(), "About Clicked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), about::class.java))
        }

        // Set click listener for Donate Now button
        binding.donateNowButton.setOnClickListener {
            val amount = binding.amountEditText.text.toString().toDoubleOrNull()
            val amountString = binding.amountEditText.text.toString()
            val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putString("AMOUNT", amountString) // You can use putDouble for API level 29 and above
            editor.apply()
            if (amount != null) {
                initiatePayment(amount)
            } else {
                Toast.makeText(requireContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun initiatePayment(amount: Double) {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_j3JubQCOxSzYx8")

        try {
            val options = JSONObject()
            options.put("name", "Reefood")
            options.put("description", "Donation")
            options.put("currency", "INR")
            options.put("amount", (amount * 100).toInt())
            val prefill = JSONObject()
            prefill.put("email", "test@razorpay.com")
            prefill.put("contact", "9999999999")
            options.put("prefill", prefill)

            checkout.open(requireActivity(), options)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun fetchTotalAmount() {
        totalAmountRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val totalAmount = dataSnapshot.getValue(Double::class.java) ?: 0.0
                // Update UI with the fetched total amount
                // For example, if you have a TextView to display the total amount
                 binding.totalFundsTextView.text = "Rs: $totalAmount"
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }

    // Other methods from your HomeFragment2 class

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        // Payment success logic
        Toast.makeText(requireContext(), "Payment Successful", Toast.LENGTH_SHORT).show()

        // Save payment details to Firebase Realtime Database
        razorpayPaymentId?.let { paymentId ->
            val amount = binding.amountEditText.text.toString().toDoubleOrNull()

        }
    }

    override fun onPaymentError(code: Int, response: String?) {
        // Payment failure logic
        Toast.makeText(requireContext(), "Payment Failed: $response", Toast.LENGTH_SHORT).show()
        // Additional logic if needed
    }
}


