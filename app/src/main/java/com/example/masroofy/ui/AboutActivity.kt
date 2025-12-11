package com.example.masroofy.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.masroofy.ui.theme.MasroofyTheme

class AboutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MasroofyTheme {
                AboutScreen()
            }
        }
    }
}

@Composable
fun AboutScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Surface(
            modifier = Modifier.size(120.dp),
            shape = CircleShape,
            color = Color(0xFFE8EAF6),
            shadowElevation = 8.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Logo",
                    tint = Color(0xFF6200EE),
                    modifier = Modifier.size(60.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Masroofy",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6200EE)
        )

        Text(
            text = "v1.0.0",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))


        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Smart Expense Tracker 🚀",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "A simple and efficient app to track your daily income and expenses. Built with modern Android technologies.",
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))



        Text("Built With:", fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TechBadge("Kotlin")
            TechBadge("Compose")
            TechBadge("Room DB")
        }

        Spacer(modifier = Modifier.weight(1f))


        Text("Developed by", fontSize = 14.sp, color = Color.Gray)
        Text("Team 29", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:muhammeddallam@gmail.com")
                    putExtra(Intent.EXTRA_SUBJECT, "Masroofy Feedback")
                }

                try {
                    context.startActivity(intent)
                } catch (e: Exception) {

                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Email, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Contact Developer")
        }
    }
}


@Composable
fun TechBadge(text: String) {
    Surface(
        color = Color(0xFFE0E0E0),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}