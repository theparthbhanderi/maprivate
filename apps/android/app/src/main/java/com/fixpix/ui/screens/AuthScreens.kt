package com.fixpix.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.fixpix.ui.components.ButtonSize
import com.fixpix.ui.components.ButtonVariant
import com.fixpix.ui.components.FixPixButton
import com.fixpix.ui.components.FixPixCard
import com.fixpix.ui.components.FixPixScaffold
import com.fixpix.ui.components.FixPixTextField
import com.fixpix.ui.theme.FixPixTheme

@Composable
fun LoginScreen(
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onLoginClick: (String, String) -> Unit,
    onSignupClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Hero Gradient
    val heroGradient = Brush.verticalGradient(
        colors = listOf(
            FixPixTheme.colors.background,
            FixPixTheme.colors.surface
        )
    )

    FixPixScaffold(
        background = Color.Transparent 
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(heroGradient)
                .padding(padding)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            FixPixCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = FixPixTheme.colors.surface
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Logo
                    Box(modifier = Modifier
                        .size(60.dp)
                        .background(FixPixTheme.colors.primary, FixPixTheme.shapes.xxl)) {
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        text = "Welcome Back",
                        style = FixPixTheme.typography.title2,
                        color = FixPixTheme.colors.textPrimary
                    )
                    Text(
                        text = "Sign in to continue editing",
                        style = FixPixTheme.typography.subhead,
                        color = FixPixTheme.colors.textSecondary,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                    
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage,
                            style = FixPixTheme.typography.caption1,
                            color = FixPixTheme.colors.error,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    // Inputs
                    FixPixTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "Email / Username",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    FixPixTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "Password",
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = null, tint = FixPixTheme.colors.textSecondary)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Actions
                    FixPixButton(
                        onClick = { onLoginClick(email, password) },
                        modifier = Modifier.fillMaxWidth(),
                        size = ButtonSize.Lg,
                        loading = isLoading
                    ) {
                        Text("Log In")
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    FixPixButton(
                        onClick = onSignupClick,
                        variant = ButtonVariant.Plain,
                        modifier = Modifier.fillMaxWidth(),
                        size = ButtonSize.Md,
                        enabled = !isLoading
                    ) {
                        Text("Create an account", color = FixPixTheme.colors.primary)
                    }
                }
            }
        }
    }
}
