package com.example.memoire3

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memoire3.data.Finance
import com.example.memoire3.data.facture
import com.example.memoire3.ui.theme.BlueStart
import com.example.memoire3.ui.theme.GreenStart
import com.example.memoire3.ui.theme.OrangeStart
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

val financeList = listOf(
    Finance(
        icon = Icons.Rounded.Star,
        name = "My\nStatus",
        background = OrangeStart
    ),

    Finance(
        icon = Icons.Rounded.Wallet,
        name = "Index\nInput",
        background = BlueStart
    ),



    Finance(
        icon = Icons.Rounded.MonetizationOn,
        name = "My\nTransactions",
        background = GreenStart
    ),
)

@Preview
@Composable
fun FinanceSection() {
    Column {
        Text(
            text = "Paiment",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        LazyRow {
            items(financeList.size) {
                FinanceItem(it)
            }
        }
    }
}

@Composable
fun FinanceItem(
    index: Int
) {


    val finance = financeList[index]
    var lastPaddingEnd = 0.dp
    if (index == financeList.size - 1) {
        lastPaddingEnd = 16.dp

    }
    var isButtonEnabled: Boolean=false
    var numcpt = stringResource(id = R.string.numcpt)

    val database = FirebaseDatabase.getInstance().getReference("facture")
    database.orderByChild("num").equalTo(numcpt.toString())
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (userSnapshot in dataSnapshot.children) {
                        val user = userSnapshot.getValue(facture::class.java)

                        if (user != null) {
                            if (!user.stat!! ) {

                                isButtonEnabled = false




                                break


                            } else {
                                isButtonEnabled = true
                                break
                            }
                        }
                    }

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {


            }
        })









    var buttonColor   = remember {
        mutableStateOf(

    if (index == 0 && isButtonEnabled) {
        Color(157, 253, 84 )
    }
    else {
        if (index == 0 && !isButtonEnabled) {
            Color(252, 117, 117 )
        }
        else{

       Color(180, 235, 242 )
        }
    })}


    Box(modifier = Modifier
        .padding(start = 16.dp, end = lastPaddingEnd)
        .clickable {
            // Handle click based on the index or finance type
            when (index) {
                0 -> {
                    // Click action for "My Status"
                    // Example: Navigate to status screen
                }

                1 -> {
                    // Click action for "Index Input"
                    // Example: Show input form
                }

                2 -> {
                    // Click action for "My Transactions"
                    // Example: Open transactions history
                }

            }
        }
    ) {

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .background(buttonColor.value)
                .size(120.dp)
                .clickable {}
                .padding(13.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(finance.background)
                    .padding(6.dp)
            ) {
                Icon(
                    imageVector = finance.icon,
                    contentDescription = finance.name,
                    tint = Color.White
                )
            }

            Text(
                text = finance.name,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        }
    }
}