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
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class RequestinboxActivity : AppCompatActivity() {

    private lateinit var tableLayout: TableLayout
    private val requestsReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("request")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requestinbox)

        tableLayout = findViewById(R.id.tableLayout1)

        // Show all pending requests
        showPendingRequests()
    }

    private fun showPendingRequests() {
        // Create a TableRow for the headings
        val headingRow = TableRow(this@RequestinboxActivity)
        headingRow.setBackgroundColor(Color.parseColor("#800080"))

        // Add TextViews for each heading
        addHeadingToRow(headingRow, "Trust Name")
        addHeadingToRow(headingRow, "Details")
        addHeadingToRow(headingRow, "Actions")

        // Add the heading row to the table layout
        tableLayout.addView(headingRow)

        // Fetch and show pending requests data
        requestsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    Log.e("Requests", "No data found for requests")
                    return
                }

                for (requestSnapshot in snapshot.children) {
                    val requestData = requestSnapshot.getValue(RequestData::class.java)

                    requestData?.let {
                        if (it.status == "Pending") {
                            val tableRow = TableRow(this@RequestinboxActivity)

                            val params = TableRow.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT
                            )
                            params.gravity = Gravity.CENTER
                            tableRow.layoutParams = params

                            // Populate TextView with trust name
                            addTextViewToRow(tableRow, it.trustname ?: "N/A")

                            // Add view details button
                            val viewDetailsButton = Button(this@RequestinboxActivity)
                            viewDetailsButton.text = "View Details"
                            viewDetailsButton.setTextColor(Color.WHITE)
                            viewDetailsButton.setBackgroundColor(Color.BLUE)
                            viewDetailsButton.setOnClickListener {
                                // Handle view details button click
                                val intent = Intent(this@RequestinboxActivity, DetailsActivity::class.java)
                                intent.putExtra("requestId", requestSnapshot.key)
                                startActivity(intent)
                            }
                            tableRow.addView(viewDetailsButton)

                            // Add accept and reject buttons under "Actions" column
                            val actionsLayout = TableRow(this@RequestinboxActivity)
                            actionsLayout.layoutParams = params

                            // Add accept button
                            val acceptButton = Button(this@RequestinboxActivity)
                            acceptButton.text = "Accept"
                            acceptButton.setTextColor(Color.WHITE)
                            acceptButton.setBackgroundColor(Color.GREEN)
                            acceptButton.setOnClickListener {
                                // Handle accept button click
                                if (requestSnapshot.key != null) {
                                    updateStatus(requestSnapshot.key!!, "Accepted")
                                    // Redirect to AcceptActivity after accepting
                                    val intent = Intent(this@RequestinboxActivity, AcceptActivity::class.java)
                                    intent.putExtra("requestId", requestSnapshot.key)
                                    startActivity(intent)
                                } else {
                                    Log.e("RequestInboxActivity", "Null requestId found when updating status")
                                }
                            }
                            actionsLayout.addView(acceptButton)

                            // Add reject button
                            val rejectButton = Button(this@RequestinboxActivity)
                            rejectButton.text = "Reject"
                            rejectButton.setTextColor(Color.WHITE)
                            rejectButton.setBackgroundColor(Color.RED)
                            rejectButton.setOnClickListener {
                                // Handle reject button click
                                if (requestSnapshot.key != null) {
                                    updateStatus(requestSnapshot.key!!, "Rejected")
                                    // Clear the data and redirect to RequestPageActivity
                                    val intent = Intent(this@RequestinboxActivity, RequestinboxActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Log.e("RequestInboxActivity", "Null requestId found when updating status")
                                }
                            }
                            actionsLayout.addView(rejectButton)

                            // Add the actionsLayout to the tableRow
                            tableRow.addView(actionsLayout)

                            // Add the tableRow to the table layout
                            tableLayout.addView(tableRow)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Requests", "Database error: ${error.message}")
            }
        })
    }

    private fun updateStatus(requestId: String, newStatus: String) {
        val requestRef = requestsReference.child(requestId)
        requestRef.child("status").setValue(newStatus)
            .addOnSuccessListener {
                Log.d("RequestInboxActivity", "Status updated successfully")
                // Once status is updated, you might want to refresh the UI or take further action
                // For example, if newStatus is "Rejected", you could remove the corresponding TableRow from the tableLayout
            }
            .addOnFailureListener { e ->
                Log.e("RequestInboxActivity", "Error updating status", e)
            }
    }

    private fun addTextViewToRow(tableRow: TableRow, s: String) {
        val textView = TextView(this@RequestinboxActivity)
        textView.text = s
        textView.setTextColor(Color.BLACK)
        textView.setBackgroundColor(Color.TRANSPARENT)

        tableRow.addView(textView)
    }

    private fun addHeadingToRow(row: TableRow, text: String) {
        val button = Button(this@RequestinboxActivity)
        button.text = text
        button.setTextColor(Color.WHITE)
        button.setBackgroundColor(Color.parseColor("#800080"))

        row.addView(button)
    }
}
