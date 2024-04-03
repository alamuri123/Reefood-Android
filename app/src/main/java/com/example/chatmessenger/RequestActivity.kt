package com.example.chatmessenger



import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatmessenger.databinding.ActivityRequestBinding
import com.google.firebase.database.*
import java.util.*

class RequestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRequestBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("request")

        binding.inputedob1.setOnClickListener {
            showDatePickerDialog()
        }

        binding.addroomsubmitbtn.setOnClickListener {
            val trustname = binding.inputname.text.toString()
            val trustdescription = binding.inputroomid.text.toString()
            val address = binding.inputmobileno.text.toString()
            val foodquantity = binding.inputedob.text.toString()
            val date = binding.inputedob1.text.toString()
            val code = binding.inputedob2.text.toString()

            if (trustname.isNotEmpty() && trustdescription.isNotEmpty() && address.isNotEmpty() && foodquantity.isNotEmpty() && date.isNotEmpty() && code.isNotEmpty()) {
                saveHomeDataToDatabase(trustname, trustdescription, address, foodquantity, date, code)
                clearDataFields() // Clear data fields after saving
            } else {
                Toast.makeText(this@RequestActivity, "All Fields are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveHomeDataToDatabase(trustname: String, trustdescription: String, address: String, foodquantity: String, date: String, code: String) {
        val id = databaseReference.push().key // Auto-incremental ID
        val requestData = RequestData(id, trustname, trustdescription, address, foodquantity, date, code)

        if (id != null) {
            databaseReference.child(id).setValue(requestData)
                .addOnSuccessListener {
                    Toast.makeText(this@RequestActivity, "Data entered into the database", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this@RequestActivity, "Error creating data entry", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this@RequestActivity, "Error creating data entry", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                binding.inputedob1.setText(selectedDate)
            },
            year, month, dayOfMonth)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun clearDataFields() {
        binding.inputname.text.clear()
        binding.inputroomid.text.clear()
        binding.inputmobileno.text.clear()
        binding.inputedob.text.clear()
        binding.inputedob1.text.clear()
        binding.inputedob2.text.clear()
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}
