package com.example.chatmessenger

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class InboxdonationActivity : AppCompatActivity() {

    private lateinit var tableLayout: TableLayout
    private val offersReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("offers")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inboxdonation) // Set the correct layout file

        tableLayout = findViewById(R.id.tableLayout)

        // Show all offers
        showOffers()
    }

    private fun showOffers() {
        // Create a TableRow for the headings
        val headingRow = TableRow(this@InboxdonationActivity)
        headingRow.setBackgroundColor(Color.parseColor("#800080"))

        // Add TextViews for each heading
        addHeadingToRow(headingRow, "NAME", Color.WHITE);
        addHeadingToRow(headingRow, "DESCRIPTION", Color.WHITE);
        addHeadingToRow(headingRow, "ADDRESS", Color.WHITE);
        addHeadingToRow(headingRow, "FOOD QUANTITY", Color.WHITE);
        addHeadingToRow(headingRow, "DATE", Color.WHITE);
        addHeadingToRow(headingRow, "CODE", Color.WHITE);

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
                        val tableRow = TableRow(this@InboxdonationActivity)

                        val params = TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                        )
                        params.gravity = Gravity.CENTER
                        tableRow.layoutParams = params

                        // Populate TextViews with offer data
                        addTextViewToRow(tableRow, it.name ?: "N/A")
                        addTextViewToRow(tableRow, it.description ?: "N/A")
                        addTextViewToRow(tableRow, it.address ?: "N/A")
                        addTextViewToRow(tableRow, it.foodquantity ?: "N/A")
                        addTextViewToRow(tableRow, it.date ?: "N/A")
                        addTextViewToRow(tableRow, it.code ?: "N/A")

                        tableLayout.addView(tableRow)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Offers", "Database error: ${error.message}")
            }
        })
    }

    private fun addHeadingToRow(row: TableRow, text: String, color: Int) {
        val textView = TextView(this@InboxdonationActivity)
        textView.text = text
        textView.textSize = 16f
        textView.setTextColor(color) // Set text color
        textView.setPadding(16, 16, 16, 16)
//        textView.setTypeface(null, Typeface.BOLD)

        row.addView(textView)
    }

    private fun addTextViewToRow(tableRow: TableRow, s: String) {
        val textView = TextView(this@InboxdonationActivity)
        textView.text = s
        textView.textSize = 16f
        textView.setTextColor(Color.BLACK)
        textView.setPadding(16, 16, 16, 16)

        tableRow.addView(textView)
    }
}
