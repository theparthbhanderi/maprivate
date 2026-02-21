package com.fixpix.android.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fixpix.android.navigation.Screen
import com.fixpix.android.ui.components.FixPixButton
import com.fixpix.android.ui.components.ButtonVariant
import com.fixpix.android.ui.components.auth.AuthInputField
import com.fixpix.android.ui.theme.FixPixTheme
import kotlinx.coroutines.delay

/**
 * RegisterScreen - Matches FixPix website mobile registration
 */
@Composable
fun RegisterScreen(
    navController: NavController
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var showContent by remember { mutableStateOf(false) }
    
    // Entry animation
    LaunchedEffect(Unit) {
        delay(100)
        showContent = true
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FixPixTheme.colors.background)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        FixPixTheme.colors.primary.copy(alpha = 0.12f),
                        Color.Transparent
                    ),
                    center = Offset(0f, 0f),
                    radius = 800f
                )
            )
            .imePadding()
    ) {
        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn(animationSpec = tween(300)) + 
                    slideInVertically(
                        animationSpec = tween(300),
                        initialOffsetY = { it / 4 }
                    )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(1f))
                
                // Logo & Title
                Text(
                    text = "FixPix",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = FixPixTheme.colors.primary,
                    letterSpacing = (-1).sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Create Account",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = FixPixTheme.colors.textMain
                )
                
                Text(
                    text = "Start restoring your memories today",
                    fontSize = 15.sp,
                    color = FixPixTheme.colors.textSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Error message
                if (error != null) {
                    Text(
                        text = error!!,
                        fontSize = 14.sp,
                        color = FixPixTheme.colors.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                
                // Username field
                AuthInputField(
                    value = username,
                    onValueChange = { 
                        username = it
                        error = null
                    },
                    placeholder = "Username",
                    leadingIcon = Icons.Outlined.Person,
                    imeAction = ImeAction.Next
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Email field
                AuthInputField(
                    value = email,
                    onValueChange = { 
                        email = it
                        error = null
                    },
                    placeholder = "Email",
                    leadingIcon = Icons.Outlined.Email,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Password field
                AuthInputField(
                    value = password,
                    onValueChange = { 
                        password = it
                        error = null
                    },
                    placeholder = "Password",
                    leadingIcon = Icons.Outlined.Lock,
                    isPassword = true,
                    imeAction = ImeAction.Next
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Confirm Password field
                AuthInputField(
                    value = confirmPassword,
                    onValueChange = { 
                        confirmPassword = it
                        error = null
                    },
                    placeholder = "Confirm Password",
                    leadingIcon = Icons.Outlined.Lock,
                    isPassword = true,
                    isError = confirmPassword.isNotEmpty() && password != confirmPassword,
                    errorMessage = if (confirmPassword.isNotEmpty() && password != confirmPassword) 
                        "Passwords don't match" else null,
                    imeAction = ImeAction.Done
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Register button
                FixPixButton(
                    onClick = {
                        when {
                            username.isBlank() || email.isBlank() || password.isBlank() -> {
                                error = "Please fill in all fields"
                            }
                            password != confirmPassword -> {
                                error = "Passwords don't match"
                            }
                            password.length < 6 -> {
                                error = "Password must be at least 6 characters"
                            }
                            else -> {
                                isLoading = true
                                // TODO: Call register API
                                // For now, navigate back to login
                                navController.popBackStack()
                            }
                        }
                    },
                    text = "Create Account",
                    fullWidth = true,
                    loading = isLoading
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Login link
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Already have an account? ",
                        fontSize = 15.sp,
                        color = FixPixTheme.colors.textSecondary
                    )
                    Text(
                        text = "Log in",
                        fontSize = 15.sp,
                        color = FixPixTheme.colors.primary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        }
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
