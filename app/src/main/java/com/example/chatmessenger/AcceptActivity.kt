package com.example.chatmessenger



import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class AcceptActivity : AppCompatActivity() {

    private lateinit var tableLayout: TableLayout
    private val requestsReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("request")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accept)

        // Initialize table layout
        tableLayout = findViewById(R.id.tableLayout1)

        // Add headings
        addHeadings()

        // Fetch and display request details
        fetchRequestDetails()
    }

    private fun addHeadings() {
        // Create a TableRow for the headings
        val headingRow = TableRow(this@AcceptActivity)
        headingRow.setBackgroundColor(Color.parseColor("#800080"))

        // Add headings
        addHeadingToRow(headingRow, "Trust Name")
        addHeadingToRow(headingRow, "Details")

        // Add the heading row to the table layout
        tableLayout.addView(headingRow)
    }

    private fun fetchRequestDetails() {
        requestsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    // Handle case when there are no requests
                    return
                }

                for (requestSnapshot in snapshot.children) {
                    val requestData = requestSnapshot.getValue(RequestData::class.java)
                    requestData?.let {
                        if (it.status == "Accepted") {
                            // Create a TableRow for the details
                            val row = TableRow(this@AcceptActivity)

                            // Add TextViews for each detail
                            addTextViewToRow(row, it.trustname ?: "N/A", true) // Trust Name in bold
                            // Add other details similarly

                            // Add view details button
                            val viewDetailsButton = Button(this@AcceptActivity)
                            viewDetailsButton.text = "View Details"
                            viewDetailsButton.setTextColor(Color.WHITE)
                            viewDetailsButton.setBackgroundColor(Color.BLUE)
                            viewDetailsButton.setOnClickListener {
                                // Handle view details button click
                                val intent = Intent(this@AcceptActivity, DetailsActivity::class.java)
                                intent.putExtra("requestId", requestSnapshot.key)
                                startActivity(intent)
                            }
                            row.addView(viewDetailsButton)

                            // Add the row to the table layout
                            tableLayout.addView(row)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun addTextViewToRow(row: TableRow, text: String, isBold: Boolean) {
        val textView = TextView(this)
        textView.text = text
        textView.setPadding(8, 8, 8, 8)
        textView.setTextSize(22f) // Set text size
//        if (isBold) {
//            textView.setTypeface(null, Typeface.BOLD) // Set text style bold
//        }
        row.addView(textView)
        textView.setTextColor(Color.BLACK)
    }

    private fun addHeadingToRow(row: TableRow, text: String) {
        val textView = TextView(this)
        textView.text = text
        textView.setPadding(8, 8, 8, 8)
        textView.setTextSize(22f) // Set text size
//        textView.setTypeface(null, Typeface.BOLD) // Set text style bold
        row.addView(textView)
    }
}
