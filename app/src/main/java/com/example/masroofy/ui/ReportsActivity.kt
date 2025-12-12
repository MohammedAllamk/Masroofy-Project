package com.example.masroofy.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.masroofy.data.AppDatabase
import com.example.masroofy.data.CategorySummary
import com.example.masroofy.ui.theme.MasroofyTheme
import kotlinx.coroutines.launch

class ReportsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getDatabase(this)
        val dao = db.expenseDao()

        setContent {
            MasroofyTheme {
                ReportsScreen(dao) { finish() }
            }
        }
    }
}

@Composable
fun ReportsScreen(dao: com.example.masroofy.data.ExpenseDao, onBack: () -> Unit) {

    var data by remember { mutableStateOf(listOf<CategorySummary>()) }
    val scope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        scope.launch { data = dao.getExpensesByCategory() }
    }

    val totalAmount = data.sumOf { it.total }

    val colors = listOf(
        Color(0xFFEF5350),
        Color(0xFF42A5F5),
        Color(0xFF66BB6A),
        Color(0xFFFFA726),
        Color(0xFFAB47BC),
        Color(0xFF8D6E63)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Analysis 📊", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }

        if (data.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No expenses yet to analyze!", color = Color.Gray)
            }
        } else {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(200.dp)) {
                    var startAngle = -90f
                    data.forEachIndexed { index, item ->
                        val sweepAngle = (item.total / totalAmount * 360).toFloat()
                        drawArc(
                            color = colors[index % colors.size],
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            style = Stroke(width = 50f)
                        )
                        startAngle += sweepAngle
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Total Spent", color = Color.Gray, fontSize = 12.sp)
                    Text(
                        "${totalAmount.toInt()} EGP", fontWeight = FontWeight.Bold, fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(data.size) { index ->
                    val item = data[index]
                    val percentage = (item.total / totalAmount * 100).toInt()
                    val color = colors[index % colors.size]


                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {

                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(color, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(item.category, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("${item.total} EGP", fontWeight = FontWeight.Bold)
                                Text("$percentage%", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}