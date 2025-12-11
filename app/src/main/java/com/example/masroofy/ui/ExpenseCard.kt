package com.example.masroofy.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.masroofy.data.Expense
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ExpenseCard(
    expense: Expense,
    onDeleteClick: () -> Unit = {}
) {

    val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    val dateString = dateFormat.format(Date(expense.date))


    val icon = when (expense.category) {
        "Food" -> "\uD83E\uDD58"
        "Cafe" -> "☕"
        "Drinks" -> "🧃"
        "Transport" -> "🚗"
        "Bills" -> "🧾"
        "Shopping" -> "🛍️"
        "Health" -> "💊"
        "Education" -> "📚"
        "Entertainment" -> "🍿"
        "Salary" -> "💰"
        "Savings" -> "🏦"
        else -> "🏷️"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (expense.isIncome) Color(0xFFE0F2F1) else Color(0xFFFFEBEE)
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = icon,
                    fontSize = 28.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))


            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = expense.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = dateString,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }


            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (expense.isIncome) "+${expense.amount}" else "-${expense.amount}",
                    color = if (expense.isIncome) Color(0xFF00C853) else Color(0xFFD50000),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp
                )
                IconButton(onClick = onDeleteClick, modifier = Modifier.size(24.dp)) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.LightGray
                    )
                }
            }
        }
    }
}