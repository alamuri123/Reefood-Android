package com.example.chatmessenger



import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatmessenger.databinding.ActivityOfferBinding
import com.google.firebase.database.*
import java.util.*

class OfferActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOfferBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfferBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("offers")

        binding.inputedob1.setOnClickListener {
            showDatePicker()
        }

        binding.addroomsubmitbtn.setOnClickListener {
            val name = binding.inputname.text.toString()
            val description = binding.inputroomid.text.toString()
            val address = binding.inputmobileno.text.toString()
            val foodquantity = binding.inputedob.text.toString()
            val date = binding.inputedob1.text.toString()
            val code = binding.inputedob2.text.toString()

            if (name.isNotEmpty() && description.isNotEmpty() && address.isNotEmpty() && foodquantity.isNotEmpty() && date.isNotEmpty() && code.isNotEmpty()) {
                saveHomeDataToDatabase(name, description, address, foodquantity, date, code)
            } else {
                Toast.makeText(this@OfferActivity, "All Fields are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveHomeDataToDatabase(name: String, description: String, address: String, foodquantity: String, date: String, code: String) {
        val id = databaseReference.push().key // Auto-incremental ID
        val offerData = OfferData(id, name, description, address, foodquantity, date, code)

        if (id != null) {
            // Check if any of the fields are null before saving to the database
            if (offerData.id != null && offerData.name != null && offerData.description != null && offerData.address != null &&
                offerData.foodquantity != null && offerData.date != null && offerData.code != null
            ) {
                databaseReference.child(id).setValue(offerData)
                    .addOnSuccessListener {
                        Toast.makeText(this@OfferActivity, "Data entered into the database", Toast.LENGTH_SHORT).show()
                        // Clearing input fields after successful data entry
                        binding.inputname.text.clear()
                        binding.inputroomid.text.clear()
                        binding.inputmobileno.text.clear()
                        binding.inputedob.text.clear()
                        binding.inputedob1.text.clear()
                        binding.inputedob2.text.clear()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@OfferActivity, "Error creating data entry", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this@OfferActivity, "Some fields are null", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this@OfferActivity, "Error creating data entry", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                // Update the EditText with the selected date
                binding.inputedob1.setText("$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear")
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}


