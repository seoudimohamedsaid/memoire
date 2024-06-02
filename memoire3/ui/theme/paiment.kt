package com.example.memoire3.ui.theme

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.memoire3.R
import com.example.memoire3.data.facture
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


@Composable
fun PaymentScreen(navController: NavController) {
    var num = stringResource(id = R.string.numf)
    var nom = stringResource(id = R.string.nom)
    var date = stringResource(id = R.string.date)
    var adresse = stringResource(id = R.string.adresse)
    var price = stringResource(id = R.string.price)

    val database = FirebaseDatabase.getInstance().getReference("facture")
    database.orderByChild("num").equalTo(num.toString())
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (userSnapshot in dataSnapshot.children) {
                        val user = userSnapshot.getValue(facture::class.java)

                        if (user != null) {
                            nom=user.nom!!
                            num=user.numf!!
                            date=user.date!!
                           price= user.montant.toString()

                        }
                    }

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {


            }
        })








    var amount by remember { mutableStateOf(price) }
    var cardNumber by remember { mutableStateOf(num) }
    var expirationDate by remember { mutableStateOf(date) }
    var name by remember { mutableStateOf(nom) }
    var address by remember { mutableStateOf(adresse) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(bottom = 50.dp) // Push everything down
    ) {
        Text(
            text = "Payment Details",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(235, 152, 78), RoundedCornerShape(8.dp))
                .padding(16.dp)
                .border(1.dp, Color.Blue, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                PaymentDetailRow("Amount", "$amount")
                PaymentDetailRow("Bill Number", cardNumber)
                PaymentDetailRow("Expiration Date", expirationDate)
                PaymentDetailRow("Name", name)
                PaymentDetailRow("Address", address)
            }
        }

        Button(
            onClick = {
                if (amount.toInt() > 0) {
                    val paymentLink = "https://buy.stripe.com/test_7sI7uD6ct29G0rC3cc"
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(paymentLink)))
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            )
        ) {
            Text(text = "Pay Now")
        }
    }
}

@Composable
fun PaymentDetailRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}
