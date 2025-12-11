package com.example.masroofy.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.masroofy.data.Expense
import com.example.masroofy.data.ExpenseDao
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(dao: ExpenseDao) {

    var recentExpenses by remember { mutableStateOf(listOf<Expense>()) }
    var totalIncome by remember { mutableStateOf(0.0) }
    var totalExpense by remember { mutableStateOf(0.0) }


    var showBottomSheet by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }


    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()


    fun refreshData() {
        scope.launch {
            recentExpenses = dao.getRecentExpenses(5)
            totalIncome = dao.getTotalAmount(true) ?: 0.0
            totalExpense = dao.getTotalAmount(false) ?: 0.0
        }
    }

    LaunchedEffect(Unit) { refreshData() }

    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Text("Masroofy", fontWeight = FontWeight.Bold, color = Color(0xFF6200EE))
                },
                actions = {

                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = Color.Black
                        )
                    }


                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(Color.White)
                    ) {

                        DropdownMenuItem(
                            text = { Text("📊  Spending Analysis") },
                            onClick = {
                                showMenu = false
                                context.startActivity(Intent(context, ReportsActivity::class.java))
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("🚀  Savings Goal (Flutter)") },
                            onClick = {
                                showMenu = false
                                val flutterPackageName = "com.example.save_calc"
                                val intent = context.packageManager.getLaunchIntentForPackage(
                                    flutterPackageName
                                )
                                if (intent != null) context.startActivity(intent)
                                else Toast.makeText(
                                    context,
                                    "Flutter App Not Found!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("ℹ️  About App") },
                            onClick = {
                                showMenu = false
                                context.startActivity(Intent(context, AboutActivity::class.java))
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF5F7FA))
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                containerColor = Color(0xFF6200EE),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .padding(paddingValues)
        ) {


            DashboardCard(totalIncome, totalExpense)

            Spacer(modifier = Modifier.height(24.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Recent Transactions", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(
                    "See All",
                    color = Color(0xFF6200EE),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        context.startActivity(Intent(context, HistoryActivity::class.java))
                    })
            }

            Spacer(modifier = Modifier.height(8.dp))


            if (recentExpenses.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📭", fontSize = 40.sp)
                        Text("No transactions yet!", color = Color.Gray)
                    }
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(bottom = 80.dp)) {
                    items(recentExpenses) { expense ->
                        ExpenseCard(expense = expense, onDeleteClick = {
                            scope.launch { dao.deleteExpense(expense); refreshData() }
                        })
                    }
                }
            }
        }


        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = Color.White
            ) {
                InputArea(
                    title = title, onTitleChange = { title = it },
                    amount = amount, onAmountChange = { amount = it },
                    category = category, onCategoryChange = { category = it },
                    onAddClick = { isIncome ->
                        if (title.isNotEmpty() && amount.isNotEmpty() && category.isNotEmpty()) {
                            scope.launch {
                                dao.insertExpense(
                                    Expense(
                                        title = title,
                                        amount = amount.toDouble(),
                                        category = category,
                                        isIncome = isIncome
                                    )
                                )
                                refreshData()
                                title = ""; amount = ""; category = ""
                                showBottomSheet = false
                                Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}


@Composable
fun DashboardCard(income: Double, expense: Double) {
    val balance = income - expense
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF6200EE)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Total Balance", color = Color.White.copy(alpha = 0.8f))
            Text(
                "$balance EGP",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text("Income ⬇️", color = Color(0xFFA5D6A7))
                    Text("+$income", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Expense ⬆️", color = Color(0xFFEF9A9A))
                    Text("-$expense", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputArea(
    title: String,
    onTitleChange: (String) -> Unit,
    amount: String,
    onAmountChange: (String) -> Unit,
    category: String,
    onCategoryChange: (String) -> Unit,
    onAddClick: (Boolean) -> Unit
) {
    val categories = listOf(
        "Food",
        "Cafe",
        "Drinks",
        "Transport",
        "Bills",
        "Shopping",
        "Health",
        "Education",
        "Entertainment",
        "Salary",
        "Savings",
        "Other"
    )
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            "Add New Transaction",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = category,
                onValueChange = {},
                label = { Text("Category") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = {
                        expanded = true
                    }) { Icon(Icons.Default.ArrowDropDown, "Drop") }
                }
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                categories.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = { onCategoryChange(option); expanded = false })
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = onAmountChange,
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Button(
                onClick = { onAddClick(false) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) { Text("Expense 💸") }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { onAddClick(true) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) { Text("Income 💰") }
        }
    }
}