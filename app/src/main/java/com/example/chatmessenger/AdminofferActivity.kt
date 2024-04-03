package com.example.chatmessenger

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatmessenger.databinding.ActivityAdminofferBinding
import com.google.firebase.database.*

class AdminofferActivity : AppCompatActivity() {

    private lateinit var tableLayout: TableLayout
    private lateinit var binding: ActivityAdminofferBinding
    private val offersReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("offers")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminofferBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_adminoffer)

        tableLayout = findViewById(R.id.tableLayout1)

        // Show all offers
        showOffers()
    }


    private fun showOffers() {
        // Create a TableRow for the headings
        val headingRow = TableRow(this@AdminofferActivity)
        headingRow.setBackgroundColor(Color.parseColor("#800080"))

        // Add Buttons for each heading
        addHeadingToRow(headingRow, "NAME")
        addHeadingToRow(headingRow, "DETAILS")
        addHeadingToRow(headingRow, "ACTIONS")

        // Add the heading row to the table layout
        tableLayout.addView(headingRow)

        // Fetch and show offers data
        offersReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    Log.e("Offers", "No data found for offers")
                    return
                }

                for (offerSnapshot in snapshot.children) {
                    val offerData = offerSnapshot.getValue(OfferData::class.java)

                    offerData?.let {
                        val tableRow = TableRow(this@AdminofferActivity)

                        val params = TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                        )
                        params.gravity = Gravity.CENTER
                        tableRow.layoutParams = params

                        // Populate Button with trust name
                        addButtonToRow(tableRow, it.name ?: "N/A")

                        // Add view details button
                        val viewDetailsButton = Button(this@AdminofferActivity)
                        viewDetailsButton.text = "View Details"
                        viewDetailsButton.setTextColor(Color.WHITE)
                        viewDetailsButton.setBackgroundColor(Color.BLUE)
                        viewDetailsButton.setOnClickListener {
                            // Handle view details button click
                            val intent = Intent(this@AdminofferActivity, OfferdetailsActivity::class.java)
                            intent.putExtra("offerId", offerSnapshot.key)
                            startActivity(intent)
                        }
                        tableRow.addView(viewDetailsButton)

                        // Add delete button
                        val deleteButton = Button(this@AdminofferActivity)
                        deleteButton.text = "Delete"
                        deleteButton.setTextColor(Color.WHITE)
                        deleteButton.setBackgroundColor(Color.RED)
                        deleteButton.setOnClickListener {
                            // Handle delete button click
                            offerSnapshot.ref.removeValue()
                                .addOnSuccessListener {
                                    Toast.makeText(this@AdminofferActivity, "Offer deleted successfully", Toast.LENGTH_SHORT).show()
                                    // Remove the TableRow from the TableLayout
                                    tableLayout.removeView(tableRow)
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this@AdminofferActivity, "Failed to delete offer", Toast.LENGTH_SHORT).show()
                                }
                        }
                        tableRow.addView(deleteButton)

                        // Add the tableRow to the table layout
                        tableLayout.addView(tableRow)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Offers", "Database error: ${error.message}")
            }
        })
    }

    private fun addButtonToRow(tableRow: TableRow, text: String) {
        val button = Button(this@AdminofferActivity)
        button.text = text
        button.setTextColor(Color.BLACK)
        button.setBackgroundColor(Color.TRANSPARENT)

        tableRow.addView(button)
    }

    private fun addHeadingToRow(row: TableRow, text: String) {
        val button = Button(this@AdminofferActivity)
        button.text = text
        button.setTextColor(Color.WHITE)
        button.setBackgroundColor(Color.parseColor("#800080"))

        row.addView(button)
    }

    override fun onBackPressed() {
        val intent = Intent(this, AdminHomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
