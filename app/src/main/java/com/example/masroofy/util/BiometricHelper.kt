package com.example.masroofy.util

import androidx.fragment.app.FragmentActivity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

class BiometricHelper(private val activity: FragmentActivity) {
    fun showBiometricPrompt(onSuccess: () -> Unit, onError: () -> Unit) {
        val executor = ContextCompat.getMainExecutor(activity)

        val biometricPrompt = BiometricPrompt(
            activity, executor, object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }

                override fun onAuthenticationError(
                    errorCode: Int, errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    when (errorCode) {
                        BiometricPrompt.ERROR_NO_BIOMETRICS, BiometricPrompt.ERROR_HW_NOT_PRESENT -> onSuccess()

                        BiometricPrompt.ERROR_USER_CANCELED, BiometricPrompt.ERROR_NEGATIVE_BUTTON -> onError()
                    }
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder().setTitle("Masroofy Security")
            .setSubtitle("Log in using fingerprint or device password")
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL).build()

        biometricPrompt.authenticate(promptInfo)
    }
}
