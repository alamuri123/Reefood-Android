package com.example.chatmessenger



import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class OfferdetailsActivity : AppCompatActivity() {

    private val offersReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("offers")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offerdetails)

        val offerId = intent.getStringExtra("offerId")
        offerId?.let { fetchOfferDetails(it) }
    }

    private fun fetchOfferDetails(offerId: String) {
        val offerReference = offersReference.child(offerId)
        offerReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val offerData = snapshot.getValue(OfferData::class.java)
                    offerData?.let { displayOfferDetails(it) }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    private fun displayOfferDetails(offerData: OfferData) {
        findViewById<TextView>(R.id.textViewTrustName).text = "${offerData.name}"
        findViewById<TextView>(R.id.textViewTrustDescription).text = "${offerData.description}"
        findViewById<TextView>(R.id.textViewAddress).text = "${offerData.address}"
        findViewById<TextView>(R.id.textViewFoodQuantity).text = "${offerData.foodquantity}"
        findViewById<TextView>(R.id.textViewDate).text = "${offerData.date}"
        findViewById<TextView>(R.id.textViewCode).text = "${offerData.code}"
    }
}
