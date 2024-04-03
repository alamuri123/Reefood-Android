package com.example.chatmessenger



import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class DetailsActivity : AppCompatActivity() {

    private val requestsReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("request")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val requestId = intent.getStringExtra("requestId")
        requestId?.let { fetchRequestDetails(it) }
    }

    private fun fetchRequestDetails(requestId: String) {
        val requestReference = requestsReference.child(requestId)
        requestReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val requestData = snapshot.getValue(RequestData::class.java)
                    requestData?.let { displayRequestDetails(it) }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    private fun displayRequestDetails(requestData: RequestData) {
        findViewById<TextView>(R.id.textViewTrustName).text = "${requestData.trustname}"
        findViewById<TextView>(R.id.textViewTrustDescription).text = "${requestData.trustdescription}"
        findViewById<TextView>(R.id.textViewAddress).text = "${requestData.address}"
        findViewById<TextView>(R.id.textViewFoodQuantity).text = "${requestData.foodquantity}"
        findViewById<TextView>(R.id.textViewDate).text = "${requestData.date}"
        findViewById<TextView>(R.id.textViewCode).text = "${requestData.code}"
    }
}

