package com.example.masroofy.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.masroofy.data.AppDatabase
import com.example.masroofy.data.Expense
import com.example.masroofy.ui.theme.MasroofyTheme
import kotlinx.coroutines.launch

class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getDatabase(this)
        val dao = db.expenseDao()

        setContent {
            MasroofyTheme {
                HistoryScreen(dao) { finish() }
            }
        }
    }
}

@Composable
fun HistoryScreen(dao: com.example.masroofy.data.ExpenseDao, onBack: () -> Unit) {
    var allExpenses by remember { mutableStateOf(listOf<Expense>()) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        allExpenses = dao.getAllExpenses()
    }

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
            Text(
                "Full History", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black
            )
        }


        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            items(allExpenses) { expense ->
                ExpenseCard(expense = expense, onDeleteClick = {
                    scope.launch {
                        dao.deleteExpense(expense)
                        allExpenses = dao.getAllExpenses()
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}