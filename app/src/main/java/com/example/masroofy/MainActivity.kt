package com.example.masroofy

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.masroofy.data.AppDatabase
import com.example.masroofy.ui.HomeScreen
import com.example.masroofy.ui.SplashScreen
import com.example.masroofy.ui.theme.MasroofyTheme
import com.example.masroofy.util.BiometricHelper

private enum class AppState {
    SPLASH, AUTHENTICATING, AUTHENTICATED
}

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(this)
        val dao = db.expenseDao()
        val biometricHelper = BiometricHelper(this)

        setContent {
            MasroofyTheme {
                var appState by remember { mutableStateOf(AppState.SPLASH) }


                LaunchedEffect(appState) {
                    if (appState == AppState.AUTHENTICATING) {
                        biometricHelper.showBiometricPrompt(onSuccess = {
                            appState = AppState.AUTHENTICATED
                        }, onError = { finish() })
                    }
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    when (appState) {
                        AppState.SPLASH -> {
                            SplashScreen {
                                appState = AppState.AUTHENTICATING
                            }
                        }

                        AppState.AUTHENTICATING -> {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    CircularProgressIndicator(Modifier.size(50.dp))
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text("Waiting for biometric scan...")
                                }
                            }
                        }

                        AppState.AUTHENTICATED -> {
                            HomeScreen(dao)
                        }
                    }
                }
            }
        }
    }
}
