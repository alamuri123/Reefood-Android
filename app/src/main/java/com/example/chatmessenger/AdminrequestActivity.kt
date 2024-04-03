package com.example.chatmessenger



import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import android.graphics.Typeface

class AdminrequestActivity : AppCompatActivity() {

    private lateinit var tableLayout: TableLayout
    private val requestsReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("request")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requestinbox)

        tableLayout = findViewById(R.id.tableLayout1)

        // Show all requests
        showRequests()
    }

    private fun showRequests() {
        // Create a TableRow for the headings
        val headingRow = TableRow(this@AdminrequestActivity)
        headingRow.setBackgroundColor(Color.parseColor("#800080"))

        // Add TextViews for each heading
        addHeadingToRow(headingRow, "TRUST NAME")
        addHeadingToRow(headingRow, "DETAILS")
        addHeadingToRow(headingRow, "ACTIONS")

        // Add the heading row to the table layout
        tableLayout.addView(headingRow)

        // Fetch and show requests data
        requestsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    Log.e("Requests", "No data found for requests")
                    return
                }

                for (requestSnapshot in snapshot.children) {
                    val requestData = requestSnapshot.getValue(RequestData::class.java)

                    requestData?.let {
                        val tableRow = TableRow(this@AdminrequestActivity)

                        val params = TableLayout.LayoutParams(
                            TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT
                        )
                        tableRow.layoutParams = params

                        // Populate TextView with trust name
                        addTextViewToRow(tableRow, it.trustname ?: "N/A")

                        // Add view details button
                        val viewDetailsButton = Button(this@AdminrequestActivity)
                        viewDetailsButton.text = "View Details"
                        viewDetailsButton.setTextColor(Color.WHITE)
                        viewDetailsButton.setBackgroundColor(Color.BLUE)
                        viewDetailsButton.setOnClickListener {
                            // Handle view details button click
                            val intent = Intent(this@AdminrequestActivity, DetailsActivity::class.java)
                            intent.putExtra("requestId", requestSnapshot.key)
                            startActivity(intent)
                        }
                        tableRow.addView(viewDetailsButton)

                        // Add delete button
                        val deleteButton = Button(this@AdminrequestActivity)
                        deleteButton.text = "Delete"
                        deleteButton.setTextColor(Color.WHITE)
                        deleteButton.setBackgroundColor(Color.RED)
                        deleteButton.setOnClickListener {
                            // Handle delete button click
                            requestSnapshot.ref.removeValue()
                                .addOnSuccessListener {
                                    Toast.makeText(this@AdminrequestActivity, "Request deleted successfully", Toast.LENGTH_SHORT).show()
                                    // Remove the TableRow from the TableLayout
                                    tableLayout.removeView(tableRow)
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this@AdminrequestActivity, "Failed to delete request", Toast.LENGTH_SHORT).show()
                                }
                        }
                        tableRow.addView(deleteButton)

                        // Add the tableRow to the table layout
                        tableLayout.addView(tableRow)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Requests", "Database error: ${error.message}")
            }
        })
    }

    private fun addTextViewToRow(tableRow: TableRow, text: String) {
        val textView = TextView(this@AdminrequestActivity)
        textView.text = text
        textView.setTextColor(Color.BLACK)
        textView.setBackgroundColor(Color.TRANSPARENT)
        textView.gravity = Gravity.CENTER_VERTICAL or Gravity.START // Set text alignment
        textView.textSize = 18f // Set text size
        textView.setPadding(16, 16, 16, 16) // Add padding

        val params = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        textView.layoutParams = params

        tableRow.addView(textView)
    }

    private fun addHeadingToRow(row: TableRow, text: String) {
        val textView = TextView(this@AdminrequestActivity)
        textView.text = text
        textView.setTextColor(Color.WHITE)
        textView.setBackgroundColor(Color.parseColor("#800080"))
        textView.gravity = Gravity.CENTER // Set text alignment
        textView.textSize = 16f // Set text size
//        textView.setTypeface(null, Typeface.BOLD)
        // Set text style to bold
        textView.setPadding(16, 16, 16, 16) // Add padding

        val params = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        row.layoutParams = params

        row.addView(textView)
    }

    override fun onBackPressed() {
        val intent = Intent(this, AdminHomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
